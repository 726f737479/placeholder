package com.dev.rosty.placeholder.common

import android.os.Process
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.RxThreadFactory
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class RxSchedulersImpl : RxSchedulers {

    private val THREAD_NAME = "db-thread-pool"
    private val DB_THREAD_KEEP_ALIVE = 10L
    private val DB_CORE_POOL_SIZE = 1
    private val DB_MAX_POOL_SIZE = 1

    private var data: Scheduler

    init {

        val dbExecutor = ThreadPoolExecutor(
                DB_CORE_POOL_SIZE,
                DB_MAX_POOL_SIZE,
                DB_THREAD_KEEP_ALIVE,
                TimeUnit.SECONDS,
                LinkedBlockingQueue<Runnable>(),
                RxThreadFactory(THREAD_NAME, Process.THREAD_PRIORITY_BACKGROUND))

        dbExecutor.allowCoreThreadTimeOut(true)
        data = Schedulers.from(dbExecutor)
    }


    override fun main() = AndroidSchedulers.mainThread()

    override fun io() = Schedulers.io()

    override fun data() = data
}