package com.dev.rosty.placeholder.presentation.posts

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.dev.rosty.placeholder.common.RxSchedulers
import com.dev.rosty.placeholder.data.DataSource
import com.dev.rosty.placeholder.entity.Post
import com.dev.rosty.placeholder.injection.AppComponent
import com.dev.rosty.placeholder.presentation.ERROR
import com.dev.rosty.placeholder.presentation.IDLE
import com.dev.rosty.placeholder.presentation.LOAD
import com.dev.rosty.placeholder.presentation.RESULT
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class   PostsViewModel : ViewModel(), AppComponent.Injectable {

    @Inject lateinit var dataSource: DataSource
    @Inject lateinit var rxSchedulers: RxSchedulers

    val posts = MutableLiveData<List<Post>>()
    val state = MutableLiveData<Int>()

    var disposable: Disposable? = null

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onCleared() {
        super.onCleared()

        disposable?.apply { if (!isDisposed) dispose() }
    }

    fun loadPosts(refresh: Boolean = false) {

        disposable?.apply { if (!isDisposed) dispose() }

        disposable = dataSource.getPosts(refresh)
                .observeOn(rxSchedulers.main())
                .doOnSubscribe { state.postValue(LOAD) }
                .subscribe(
                        { onSuccess(it) },
                        { onError() })
    }

    private fun onSuccess(posts: List<Post>) {
        this.posts.value = posts
        this.state.value = RESULT
    }

    private fun onError() {
        this.state.value = ERROR
    }
}