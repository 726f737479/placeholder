package com.dev.rosty.placeholder.data.remote

import com.dev.rosty.placeholder.entity.Comment
import com.dev.rosty.placeholder.entity.Post
import com.dev.rosty.placeholder.entity.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceholderService {

    companion object {

        val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    @GET("posts")
    fun getPosts(): Single<List<Post>>

    @GET("users/{id}")
    fun getUser(@Path("id") id: Int): Single<User>

    @GET("posts/{id}/comments")
    fun getCommentsToPost(@Path("id") postId: Int): Single<List<Comment>>
}