package com.dev.rosty.placeholder.presentation.details

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.transition.TransitionInflater
import android.view.View
import com.dev.rosty.placeholder.R
import com.dev.rosty.placeholder.entity.Comment
import com.dev.rosty.placeholder.entity.Post
import com.dev.rosty.placeholder.entity.User
import com.dev.rosty.placeholder.presentation.BaseFragment
import com.dev.rosty.placeholder.presentation.ERROR
import com.dev.rosty.placeholder.presentation.LOAD
import com.dev.rosty.placeholder.presentation.RESULT
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : BaseFragment<DetailsViewModel>() {

    private val adapter = CommentsAdapter()

    private var userId: Int = 0
    private var postId: Int = 0

    override fun getLayoutRes() = R.layout.fragment_details

    override fun getViewModelClass() = DetailsViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setTransitionName(container, postId.toString())

        rvComments.adapter = adapter
        btnRetry.setOnClickListener {  viewModel.loadData(false, userId, postId) }
        btnClose.setOnClickListener {  activity?.onBackPressed() }
        refresh.setOnRefreshListener { viewModel.loadData(true, userId, postId) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transition = TransitionInflater.from(context)
                .inflateTransition(android.R.transition.move)

        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition

        arguments!!.let {
            userId = it.getInt(ARG_USER_ID)
            postId = it.getInt(ARG_POST_ID)
        }

        viewModel.loadData(false, userId, postId)

        viewModel.comments.observe(this, Observer<List<Comment>> { adapter.submitList(it) })
        viewModel.state.observe(this, Observer<Int> { setupState(it) })
        viewModel.post.observe(this, Observer<Post> { tvTitle.text = it?.title })
        viewModel.user.observe(this, Observer<User> {

            tvName.text = it?.name
            tvMail.text = it?.email
            tvLocation.text = it?.address?.street.plus( ", ").plus(it?.address?.city)
        })
    }

    private fun setupState(state: Int?) {

        error.visibility = if (state == ERROR) View.VISIBLE else View.GONE
        progress.visibility = if (state == LOAD) View.VISIBLE else View.GONE
        content.visibility = if (state == RESULT) View.VISIBLE else View.GONE
        refresh.isRefreshing = false
        refresh.isEnabled = state == RESULT
    }

    companion object {

        private const val ARG_USER_ID = "user_id"
        private const val ARG_POST_ID = "post_id"

        @JvmStatic
        fun newInstance(userId: Int, postId: Int) =
                DetailsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_USER_ID, userId)
                        putInt(ARG_POST_ID, postId)
                    }
                }
    }
}
