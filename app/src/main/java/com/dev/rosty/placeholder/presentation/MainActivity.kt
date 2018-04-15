package com.dev.rosty.placeholder.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dev.rosty.placeholder.R
import com.dev.rosty.placeholder.presentation.posts.PostsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {

            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, PostsFragment())
                    .commit()
        }
    }
}
