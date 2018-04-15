package com.dev.rosty.placeholder.data.local

import com.dev.rosty.placeholder.entity.Comment
import com.dev.rosty.placeholder.entity.Post
import com.dev.rosty.placeholder.entity.User

interface CacheManager {

    fun savePosts(posts: List<Post>)
    fun getPosts(): List<Post>
    fun getPost(id: Int): Optional<Post>

    fun saveUser(user: User)
    fun getUser(id: Int): Optional<User>

    fun saveComments(comments: List<Comment>)
    fun getCommentsToPost(postId: Int): List<Comment>
}
