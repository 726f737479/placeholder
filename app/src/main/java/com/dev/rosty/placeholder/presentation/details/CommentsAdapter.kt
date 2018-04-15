package com.dev.rosty.placeholder.presentation.details

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.rosty.placeholder.R
import com.dev.rosty.placeholder.entity.Comment
import com.dev.rosty.placeholder.entity.Post
import kotlinx.android.synthetic.main.item_post.view.*


class CommentsAdapter : ListAdapter<Comment, CommentsAdapter.ViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_post, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemCount == position + 1, getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(isLast: Boolean, comment: Comment) {

            itemView.tvTitle.text = comment.name
            itemView.tvBody.text = comment.body
            itemView.divider.visibility = if (isLast) View.INVISIBLE else View.VISIBLE
        }
    }

    class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {

        override fun areItemsTheSame(oldItem: Comment?, newItem: Comment?)
                = oldItem?.id == newItem?.id

        override fun areContentsTheSame(oldItem: Comment?, newItem: Comment?)
                = oldItem?.equals(newItem) ?: false
    }
}