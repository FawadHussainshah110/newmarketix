package com.example.marketix.di.module

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
import com.example.marketix.data.repository.PreferencesRepositoryImp
import com.example.marketix.data.repository.SettingsRepositoryImp
import com.example.marketix.data.repository.UserRepositoryImp
import com.example.marketix.data.source.local.prefs.PreferencesHelper
import com.example.marketix.data.source.remote.RetrofitService
import com.example.marketix.domain.model.DataOrEmptyArray
import com.example.marketix.domain.model.DataOrEmptyArrayDeserializer
import com.example.marketix.domain.repository.AccountsRepository
import com.example.marketix.domain.repository.AnnouncementsRepository
import com.example.marketix.domain.repository.CoursesRepository
import com.example.marketix.domain.repository.HistoryRepository
import com.example.marketix.domain.repository.MarketsRepository
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.domain.repository.SettingsRepository
import com.example.marketix.domain.repository.UserRepository
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
import javax.inject.Singleton

@Module(includes = [ApplicationModule::class])
open class NetworkModule {

    @Provides
    @Singleton
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
    open fun providesOkHttpClient(context: Context, isNetworkAvailable: Boolean): OkHttpClient {
        val cacheSize = (5 * 1024 * 1024).toLong()
        val mCache = Cache(context.cacheDir, cacheSize)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .cache(mCache) // make your app offline-friendly without a database!
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .addInterceptor { chain ->
                var request = chain.request()
                /* If there is Internet, get the cache that was stored 5 seconds ago.
                 * If the cache is older than 5 seconds, then discard it,
                 * and indicate an error in fetching the response.
                 * The 'max-age' attribute is responsible for this behavior.
                 */
                request = if (isNetworkAvailable) request.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 5).build()
                /*If there is no Internet, get the cache that was stored 7 days ago.
                 * If the cache is older than 7 days, then discard it,
                 * and indicate an error in fetching the response.
                 * The 'max-stale' attribute is responsible for this behavior.
                 * The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
                 */
                else request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                ).build()
                chain.proceed(request)
            }
        return client.build()
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
    open fun provideService(retrofit: Retrofit): RetrofitService {
        return retrofit.create(RetrofitService::class.java)
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

}