package com.dev.rosty.placeholder.data.local

import com.dev.rosty.placeholder.entity.Post
import com.dev.rosty.placeholder.posts
import org.junit.Test

import org.junit.Assert.*

class CacheManagerImplTest {

    @Test
    fun getPost() {

        val cacheManager = CacheManagerImpl()

        cacheManager.savePosts(posts)
        assertTrue(cacheManager.getPost(2).hasValue())
        assertFalse(cacheManager.getPost(6).hasValue())
    }

    @Test
    fun getPosts() {

        val cacheManager = CacheManagerImpl()

        assertNotNull(cacheManager.getPosts())
        cacheManager.savePosts(posts)
        assertNotNull(cacheManager.getPosts())
    }

    @Test
    fun savePosts() {

        val cacheManager = CacheManagerImpl()

        cacheManager.savePosts(posts)
        assertEquals(cacheManager.getPosts(), posts)
    }
}