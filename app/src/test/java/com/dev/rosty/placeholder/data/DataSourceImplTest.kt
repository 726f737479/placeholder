package com.dev.rosty.placeholder.data

import com.dev.rosty.placeholder.common.RxSchedulers
import com.dev.rosty.placeholder.data.local.CacheManager
import com.dev.rosty.placeholder.data.local.Optional
import com.dev.rosty.placeholder.data.remote.PlaceholderService
import com.dev.rosty.placeholder.entity.User
import com.dev.rosty.placeholder.user
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Test

import org.junit.Before
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times
import java.util.concurrent.TimeUnit

class DataSourceImplTest {

    @Mock private lateinit var placeholderService: PlaceholderService
    @Mock private lateinit var cacheManager: CacheManager

    private lateinit var dataSource: DataSource

    private val scheduler  = TestScheduler()
    private val schedulers = object : RxSchedulers {

        override fun main(): Scheduler { return scheduler }

        override fun io(): Scheduler { return scheduler }

        override fun data(): Scheduler { return scheduler }
    }

    @Before
    @Throws(Exception::class) fun setUp() {

        MockitoAnnotations.initMocks(this)

        dataSource = DataSourceImpl(cacheManager, placeholderService, schedulers)
    }

    @Test
    fun getUserFromService() {

        Mockito.`when`(cacheManager.getUser(1)).thenReturn(Optional())
        Mockito.`when`(placeholderService.getUser(1)).thenReturn(Single.just(user))

        val observer = TestObserver<User>()

        dataSource.getUser(false, 1)
                .toObservable()
                .subscribe(observer)

        scheduler.advanceTimeBy(0, TimeUnit.SECONDS)

        observer.assertSubscribed()
        observer.assertNoErrors()

        Mockito.verify<CacheManager>(cacheManager).saveUser(user)
        Mockito.verify<PlaceholderService>(placeholderService).getUser(1)
    }

    @Test
    fun getUserFromCache() {

        Mockito.`when`(cacheManager.getUser(1)).thenReturn(Optional(user))
        Mockito.`when`(placeholderService.getUser(1)).thenReturn(Single.just(user))

        val observer = TestObserver<User>()

        dataSource.getUser(false, 1)
                .toObservable()
                .subscribe(observer)

        scheduler.advanceTimeBy(0, TimeUnit.SECONDS)

        observer.assertSubscribed()
        observer.assertNoErrors()

        verify(cacheManager, Times(0)).saveUser(user)
        verify(placeholderService, Times(0)).getUser(1)
    }
}