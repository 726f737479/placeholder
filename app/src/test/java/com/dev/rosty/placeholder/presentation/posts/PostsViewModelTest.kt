package com.dev.rosty.placeholder.presentation.posts

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.dev.rosty.placeholder.common.RxSchedulers
import com.dev.rosty.placeholder.data.DataSource
import com.dev.rosty.placeholder.posts
import com.dev.rosty.placeholder.presentation.ERROR
import com.dev.rosty.placeholder.presentation.RESULT
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit


class PostsViewModelTest {

    private lateinit var viewModel: PostsViewModel

    @Mock
    private lateinit var  dataSource: DataSource

    private val scheduler  = TestScheduler()
    private val schedulers = object : RxSchedulers {

        override fun main(): Scheduler { return scheduler }

        override fun io(): Scheduler { return scheduler }

        override fun data(): Scheduler { return scheduler }
    }

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class) fun setUp() {

        MockitoAnnotations.initMocks(this)

        viewModel = PostsViewModel()
        viewModel.dataSource = dataSource
        viewModel.rxSchedulers = schedulers
    }

    @Test
    fun loadPostsError() {

        Mockito.`when`(dataSource.getPosts(false)).thenReturn(Single.just(posts))

        viewModel.loadPosts(false)

        scheduler.advanceTimeBy(0, TimeUnit.SECONDS)

        assertEquals(viewModel.state.value, RESULT)
        assertEquals(viewModel.posts.value, posts)
    }

    @Test
    fun loadPostsSuccess() {

        Mockito.`when`(dataSource.getPosts(false)).thenReturn(Single.error(RuntimeException()))

        viewModel.loadPosts(false)

        scheduler.advanceTimeBy(0, TimeUnit.SECONDS)

        assertEquals(viewModel.state.value, ERROR)
        assertEquals(viewModel.posts.value, null)
    }
}