package com.dev.rosty.placeholder.common

import io.reactivex.Scheduler

interface RxSchedulers {

    fun main(): Scheduler

    fun io(): Scheduler

    fun data(): Scheduler
}