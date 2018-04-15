package com.dev.rosty.placeholder.presentation.posts

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.rosty.placeholder.R
import com.dev.rosty.placeholder.entity.Post
import kotlinx.android.synthetic.main.item_post.view.*


class PostsAdapter(private val clickListener: (Post, View) -> Unit)
    : ListAdapter<Post, PostsAdapter.ViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_post, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemCount == position + 1, getItem(position), clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(isLast: Boolean, post: Post, clickListener: (Post, View) -> Unit) {

            itemView.transitionName = post.id.toString()

            itemView.tvTitle.text = post.title
            itemView.tvBody.text = post.body
            itemView.divider.visibility = if (isLast) View.INVISIBLE else View.VISIBLE
            itemView.setOnClickListener { clickListener.invoke(post, itemView) }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post?, newItem: Post?)
                = oldItem?.id == newItem?.id

        override fun areContentsTheSame(oldItem: Post?, newItem: Post?)
                = oldItem?.equals(newItem) ?: false
    }
}