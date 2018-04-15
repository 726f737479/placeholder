package com.dev.rosty.placeholder.presentation.details

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.dev.rosty.placeholder.common.RxSchedulers
import com.dev.rosty.placeholder.data.DataSource
import com.dev.rosty.placeholder.entity.Comment
import com.dev.rosty.placeholder.entity.Post
import com.dev.rosty.placeholder.entity.User
import com.dev.rosty.placeholder.injection.AppComponent
import com.dev.rosty.placeholder.presentation.ERROR
import com.dev.rosty.placeholder.presentation.IDLE
import com.dev.rosty.placeholder.presentation.LOAD
import com.dev.rosty.placeholder.presentation.RESULT
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.functions.Function3
import javax.inject.Inject

class DetailsViewModel : ViewModel(), AppComponent.Injectable {

    @Inject lateinit var dataSource: DataSource
    @Inject lateinit var rxSchedulers: RxSchedulers

    val comments = MutableLiveData<List<Comment>>()
    val user = MutableLiveData<User>()
    val post = MutableLiveData<Post>()
    val state = MutableLiveData<Int>()

    var disposable: Disposable? = null

    init { state.value = IDLE }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onCleared() {
        super.onCleared()

        disposable?.apply { if (!isDisposed) dispose() }
    }

    fun loadData(refresh: Boolean = false, userId: Int, postId: Int) {

        disposable?.apply { if (!isDisposed) dispose() }

        disposable = Single.zip(
                dataSource.getUser(refresh, userId),
                dataSource.getCommentsToPost(refresh, postId),
                dataSource.getPost(postId),
                Function3<User, List<Comment>, Post, Triple<User, List<Comment>, Post>>
                { t1, t2, t3 -> Triple(t1, t2, t3) })

                .observeOn(rxSchedulers.main())
                .doOnSubscribe { state.postValue(LOAD) }
                .subscribe(
                        { onSuccess(it) },
                        { onError() })
    }

    private fun onSuccess(triple: Triple<User, List<Comment>, Post>) {
        this.user.value = triple.first
        this.comments.value = triple.second
        this.post.value = triple.third
        this.state.value = RESULT
    }

    private fun onError() {
        this.state.value = ERROR
    }
}