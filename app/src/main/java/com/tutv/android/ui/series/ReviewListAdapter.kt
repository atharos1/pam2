package com.tutv.android.ui.series

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tutv.android.R
import com.tutv.android.domain.Review

class ReviewListAdapter(var reviewList: List<Review>,
                        var reviewLikeClickedListener: ReviewLikeClickedListener?) :
        RecyclerView.Adapter<ReviewListAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviewList[position]
        holder.setReviewName(review.user.userName)
        holder.setReviewBody(review.body)
        holder.setReviewLiked(review.loggedInUserLikes ?: false)
        holder.setLikes(review.likes)
        holder.setListenerPropagation { reviewLikeClickedListener?.onClick(review) }
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val reviewNameTextView: TextView? = itemView.findViewById(R.id.review_name)
        private val reviewBodyTextView: TextView? = itemView.findViewById(R.id.review_body)
        private val reviewLikedImageView: ImageView? = itemView.findViewById(R.id.like_button)
        private val reviewLikesTextView: TextView? = itemView.findViewById(R.id.likes_count)

        fun setReviewName(name: String?) {
            reviewNameTextView?.text = name
        }

        fun setReviewBody(body: String?) {
            reviewBodyTextView?.text = body
        }

        fun setReviewLiked(liked: Boolean?) {
            if (liked == true)
                reviewLikedImageView?.setImageResource(R.drawable.ic_baseline_thumb_up_24_filled)
            else
                reviewLikedImageView?.setImageResource(R.drawable.ic_baseline_thumb_up_24)
        }

        fun setLikes(liked: Long?) {
            reviewLikesTextView?.text = "$liked"
        }

        fun setListenerPropagation(c: Callback?) {
            reviewLikedImageView?.setOnClickListener{ c?.call() }
        }
    }
}