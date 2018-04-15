package com.dev.rosty.placeholder.data

import com.dev.rosty.placeholder.common.RxSchedulers
import com.dev.rosty.placeholder.data.local.CacheManager
import com.dev.rosty.placeholder.data.local.Optional
import com.dev.rosty.placeholder.data.remote.PlaceholderService
import com.dev.rosty.placeholder.entity.Comment
import com.dev.rosty.placeholder.entity.Post
import com.dev.rosty.placeholder.entity.User
import io.reactivex.Single

class DataSourceImpl(
        private val cacheManager: CacheManager,
        private val placeholderService: PlaceholderService,
        private val rxSchedulers: RxSchedulers
) : DataSource {

    override fun getUser(refresh: Boolean, id: Int): Single<User> {

        return Single.just(cacheManager.getUser(id))
                .subscribeOn(rxSchedulers.data())
                .observeOn(rxSchedulers.io())
                .flatMap {

                    if (!it.hasValue() || refresh) {

                        return@flatMap placeholderService.getUser(id)
                                .observeOn(rxSchedulers.data())
                                .doOnSuccess { cacheManager.saveUser(it) }
                                .observeOn(rxSchedulers.io())

                    } else return@flatMap Single.just(it.value)
                }

    }

    override fun getCommentsToPost(refresh: Boolean, postId: Int): Single<List<Comment>> {

        return Single.just(cacheManager.getCommentsToPost(postId))
                .subscribeOn(rxSchedulers.data())
                .observeOn(rxSchedulers.io())
                .flatMap {

                    if (it.isEmpty() || refresh) {

                        return@flatMap placeholderService.getCommentsToPost(postId)
                                .observeOn(rxSchedulers.data())
                                .doOnSuccess { cacheManager.saveComments(it) }
                                .observeOn(rxSchedulers.io())

                    } else return@flatMap Single.just(it)
                }
    }

    override fun getPosts(refresh: Boolean): Single<List<Post>> {

        return Single.just(cacheManager.getPosts())
                .subscribeOn(rxSchedulers.data())
                .observeOn(rxSchedulers.io())
                .flatMap {

                    if (it.isEmpty() || refresh) {

                        return@flatMap placeholderService.getPosts()
                                .observeOn(rxSchedulers.data())
                                .doOnSuccess { cacheManager.savePosts(it) }
                                .observeOn(rxSchedulers.io())

                    } else return@flatMap Single.just(it)
                }
    }

    override fun getPost(id: Int): Single<Post> {

        return Single.just(cacheManager.getPost(id))
                .subscribeOn(rxSchedulers.data())
                .map { it.value }
    }
}