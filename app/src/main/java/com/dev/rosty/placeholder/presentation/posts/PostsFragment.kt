package com.dev.rosty.placeholder.presentation.posts

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.rosty.placeholder.PlaceholderApp

import com.dev.rosty.placeholder.R
import com.dev.rosty.placeholder.entity.Post
import com.dev.rosty.placeholder.injection.VMFactory
import com.dev.rosty.placeholder.presentation.*
import com.dev.rosty.placeholder.presentation.details.DetailsFragment
import kotlinx.android.synthetic.main.fragment_posts.*

class PostsFragment : BaseFragment<PostsViewModel>() {

    private val adapter = PostsAdapter { p, v -> openDetails(p, v) }

    override fun getLayoutRes() = R.layout.fragment_posts

    override fun getViewModelClass() = PostsViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPosts.adapter = adapter
        btnRetry.setOnClickListener { viewModel.loadPosts() }
        refresh.setOnRefreshListener { viewModel.loadPosts(true) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadPosts()

        viewModel.posts.observe(this, Observer<List<Post>> { adapter.submitList(it) })
        viewModel.state.observe(this, Observer<Int> { setupState(it) })
    }

    private fun setupState(state: Int?) {

        error.visibility = if (state == ERROR) View.VISIBLE else View.GONE
        progress.visibility = if (state == LOAD) View.VISIBLE else View.GONE
        rvPosts.visibility = if (state == RESULT) View.VISIBLE else View.GONE
        refresh.isRefreshing = false
        refresh.isEnabled = state == RESULT
    }

    private fun openDetails(post: Post, view: View) {

        fragmentManager!!.beginTransaction()
                .addSharedElement(view, post.id.toString())
                .replace(R.id.container, DetailsFragment.newInstance(post.userId, post.id))
                .addToBackStack(DetailsFragment::class.java.simpleName)
                .commit()
    }
}
