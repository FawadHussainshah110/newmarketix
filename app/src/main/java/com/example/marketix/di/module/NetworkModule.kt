package com.example.marketix.di.module

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.marketix.BuildConfig
import com.example.marketix.data.mapper.AccountsMapper
import com.example.marketix.data.mapper.AnnouncementsMapper
import com.example.marketix.data.mapper.CoursesMapper
import com.example.marketix.data.mapper.HistoryMapper
import com.example.marketix.data.mapper.MarketsMapper
import com.example.marketix.data.mapper.SettingsMapper
import com.example.marketix.data.mapper.UserMapper
import com.example.marketix.data.repository.AccountsRepositoryImp
import com.example.marketix.data.repository.AnnouncementsRepositoryImp
import com.example.marketix.data.repository.CoursesRepositoryImp
import com.example.marketix.data.repository.HistoryRepositoryImp
import com.example.marketix.data.repository.MarketsRepositoryImp
import com.example.marketix.data.repository.PaymentRepositoryImp
import com.example.marketix.data.repository.PreferencesRepositoryImp
import com.example.marketix.data.repository.SettingsRepositoryImp
import com.example.marketix.data.repository.UserRepositoryImp
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.data.source.local.prefs.PreferencesHelper
import com.example.marketix.data.source.remote.NowPaymentService
import com.example.marketix.data.source.remote.RetrofitService
import com.example.marketix.domain.model.DataOrEmptyArray
import com.example.marketix.domain.model.DataOrEmptyArrayDeserializer
import com.example.marketix.domain.repository.AccountsRepository
import com.example.marketix.domain.repository.AnnouncementsRepository
import com.example.marketix.domain.repository.CoursesRepository
import com.example.marketix.domain.repository.HistoryRepository
import com.example.marketix.domain.repository.MarketsRepository
import com.example.marketix.domain.repository.PaymentRepository
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.domain.repository.SettingsRepository
import com.example.marketix.domain.repository.UserRepository
import com.example.marketix.presentation.courselearning.CourseLearningViewModel
import com.example.marketix.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ApplicationModule::class])
open class NetworkModule {

    @Provides
    @Singleton
    @Named("nowPaymentRetrofit")
    fun provideNowPaymentRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.NOWPAYMENT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNowPaymentService(@Named("nowPaymentRetrofit") retrofit: Retrofit): NowPaymentService {
        return retrofit.create(NowPaymentService::class.java)
    }

    @Provides
    @Singleton
    @Named("marketixRetrofit")
    open fun providesRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.configCheck())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(
                            DataOrEmptyArray::class.java,
                            DataOrEmptyArrayDeserializer()
                        )
                        .create()
                )
            )
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideService(@Named("marketixRetrofit") retrofit: Retrofit): RetrofitService {
        return retrofit.create(RetrofitService::class.java)
    }


    @Provides
    @Singleton
    fun provideNowPaymentOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-api-key", Constants.NOWPAYMENT_API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    open fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    open fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    open fun providesRxJavaCallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Singleton
    @Provides
    fun providePaymentRepository(
        nowPaymentService: NowPaymentService,
        schedulersProvider: SchedulersProvider,
        preferencesRepository: PreferencesRepository
    ): PaymentRepository {
        return PaymentRepositoryImp(nowPaymentService, schedulersProvider, preferencesRepository)
    }

    @Provides
    @Singleton
    open fun provideIsNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    @Singleton
    @Provides
    open fun providePreferencesRepository(
        preferencesHelper: PreferencesHelper
    ): PreferencesRepository {
        return PreferencesRepositoryImp(preferencesHelper)
    }

    @Singleton
    @Provides
    open fun provideUserRepositoryImp(
        retrofitService: RetrofitService, userMapper: UserMapper
    ): UserRepository {
        return UserRepositoryImp(retrofitService) { userMapper }
    }

    @Singleton
    @Provides
    open fun provideSettingsRepositoryImp(
        retrofitService: RetrofitService, settingsMapper: SettingsMapper
    ): SettingsRepository {
        return SettingsRepositoryImp(retrofitService) { settingsMapper }
    }

    @Singleton
    @Provides
    open fun provideAccountsRepositoryImp(
        retrofitService: RetrofitService, accountsMapper: AccountsMapper
    ): AccountsRepository {
        return AccountsRepositoryImp(retrofitService) { accountsMapper }
    }

    @Singleton
    @Provides
    open fun provideAnnouncementsRepositoryImp(
        retrofitService: RetrofitService, announcementsMapper: AnnouncementsMapper
    ): AnnouncementsRepository {
        return AnnouncementsRepositoryImp(retrofitService) { announcementsMapper }
    }

    @Singleton
    @Provides
    open fun provideHistoryRepositoryImp(
        retrofitService: RetrofitService, historyMapper: HistoryMapper
    ): HistoryRepository {
        return HistoryRepositoryImp(retrofitService) { historyMapper }
    }

    @Singleton
    @Provides
    open fun provideCoursesRepositoryImp(
        retrofitService: RetrofitService, coursesMapper: CoursesMapper
    ): CoursesRepository {
        return CoursesRepositoryImp(retrofitService) { coursesMapper }
    }

    @Singleton
    @Provides
    open fun provideMarketsRepositoryImp(
        retrofitService: RetrofitService, marketsMapper: MarketsMapper
    ): MarketsRepository {
        return MarketsRepositoryImp(retrofitService) { marketsMapper }
    }

    @Provides
    @Singleton
    fun provideCourseLearningViewModel(
        context: Context,
        preferencesRepository: PreferencesRepository,
        coursesRepository: CoursesRepository,
        schedulersProvider: SchedulersProvider,
        paymentRepository: PaymentRepository
    ): CourseLearningViewModel {
        return CourseLearningViewModel(
            context,
            preferencesRepository,
            coursesRepository,
            schedulersProvider,
            paymentRepository
        )
    }
}