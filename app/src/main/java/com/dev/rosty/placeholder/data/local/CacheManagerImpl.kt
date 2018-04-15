package com.dev.rosty.placeholder.data.local

import com.dev.rosty.placeholder.entity.Comment
import com.dev.rosty.placeholder.entity.Post
import com.dev.rosty.placeholder.entity.User

class CacheManagerImpl : CacheManager {

    private var posts: List<Post> = ArrayList()
    private var comments: MutableSet<Comment> = HashSet()
    private var users: MutableSet<User> = HashSet()


    override fun getPost(id: Int) = Optional(posts.find { it.id == id })

    override fun getPosts() = posts

    override fun savePosts(posts: List<Post>) {
        this.posts = posts
    }

    override fun saveUser(user: User) {
        users.add(user)
    }

    override fun getUser(id: Int) = Optional(users.find { it.id == id })

    override fun saveComments(comments: List<Comment>) {
        this.comments.addAll(comments)
    }

    override fun getCommentsToPost(postId: Int) = comments.filter { it.postId == postId }
}