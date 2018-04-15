package com.dev.rosty.placeholder.data

import com.dev.rosty.placeholder.entity.Comment
import com.dev.rosty.placeholder.entity.Post
import com.dev.rosty.placeholder.entity.User
import io.reactivex.Single

interface DataSource {

    fun getPosts(refresh: Boolean = false): Single<List<Post>>
    fun getPost(id: Int): Single<Post>
    fun getUser(refresh: Boolean = false, id: Int): Single<User>
    fun getCommentsToPost(refresh: Boolean = false, postId: Int): Single<List<Comment>>
}