package com.example.marketix

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.example.marketix.di.component.DaggerApplicationComponent
import com.example.marketix.localehelper.Lingver
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


class MainApplication : Application(), HasAndroidInjector {

    private val TAG = MainApplication::class.java.name

    @Inject
    lateinit var lingver: Lingver

    @Inject
    lateinit var calligraphyInterceptor: CalligraphyInterceptor

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

        DaggerApplicationComponent.builder().application(this).build().inject(this)
        DaggerApplicationComponent.builder().application(this).build().callbacks()
            .forEach { registerActivityLifecycleCallbacks(it) }

        EventBus.builder().installDefaultEventBus()

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(calligraphyInterceptor)
                .build()
        )

//        RxJavaPlugins.setErrorHandler { t: Throwable? ->
//            logger.log(Level.SEVERE, "null", t)
//        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)

    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

}