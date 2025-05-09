package com.example.marketix.di.module

import android.app.Application
import android.content.Context
import com.example.marketix.data.schedulersprovider.SchedulersFacade
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.data.source.local.prefs.PreferencesHelper
import com.example.marketix.data.source.local.prefs.PreferencesHelperImp
import com.example.marketix.di.builder.ViewModelFactoryBuilder
import com.example.marketix.localehelper.Lingver
import com.example.marketix.util.Constants
import com.yariksoffice.lingver.store.PreferenceLocaleStore
import dagger.Module
import dagger.Provides
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import java.util.Locale
import javax.inject.Singleton

@Module(includes = [ViewModelFactoryBuilder::class])
open class ApplicationModule {

    @Singleton
    @Provides
    open fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    open fun providerScheduler(schedulersFacade: SchedulersFacade): SchedulersProvider {
        return SchedulersFacade()
    }

    @Singleton
    @Provides
    open fun provideSharedPreferences(context: Context): PreferencesHelper {
        return PreferencesHelperImp(
            context.getSharedPreferences(
                PreferencesHelper.PREFERENCEs_NAME, Context.MODE_PRIVATE
            )
        )
    }

    @Provides
    @Singleton
    open fun provideCalligraphyDefaultConfig(): CalligraphyConfig {
        return CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/NexaLight.otf")
            .setFontAttrId(io.github.inflationx.calligraphy3.R.attr.fontPath)
            .setFontMapper { font -> font }
            .build()
    }

    @Provides
    @Singleton
    open fun provideCalligraphyInterceptor(calligraphyConfig: CalligraphyConfig): CalligraphyInterceptor {
        return CalligraphyInterceptor(calligraphyConfig)
    }

    @Provides
    @Singleton
    open fun provideLingverPreferenceLocaleStore(context: Context): PreferenceLocaleStore {
        return PreferenceLocaleStore(context, Locale(Constants.LANGUAGE_ENGLISH))
    }

    @Provides
    @Singleton
    open fun provideLingver(application: Application, store: PreferenceLocaleStore): Lingver {
        return Lingver.init(application, store)
    }

}