package com.example.marketix.data.schedulersprovider

import io.reactivex.Scheduler

interface SchedulersProvider {

    fun ui(): Scheduler
    fun io(): Scheduler
    fun computation(): Scheduler

}