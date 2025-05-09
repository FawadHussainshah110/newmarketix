package com.example.marketix.di.module

import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
interface FragmentModule {

    //    @ContributesAndroidInjector(
//        modules = [
//            HomeNewOrdersFragmentProvider::class
//            HomeDeliveredOrderFragment::class
//        ]
//    )
//    @ContributesAndroidInjector
//    fun homeFragment(): HomeFragment

}