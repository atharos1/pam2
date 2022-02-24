package com.tutv.android.ui.series

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tutv.android.R
import com.tutv.android.domain.Review

class ReviewListAdapter(var reviewList: List<Review>) :
        RecyclerView.Adapter<ReviewListAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setReviewName(/*reviewList[position].user.userName*/ "UserPiola123")
        holder.setReviewBody(reviewList[position].body)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val reviewNameTextView: TextView? = itemView.findViewById(R.id.review_name)
        private val reviewBodyTextView: TextView? = itemView.findViewById(R.id.review_body)
        private val viewedCheckmarkImageView: ImageView? = itemView.findViewById(R.id.viewed_checkmark)

        fun setReviewName(name: String?) {
            reviewNameTextView?.text = name
            viewedCheckmarkImageView?.visibility = View.INVISIBLE
        }

        fun setReviewBody(body: String?) {
            reviewBodyTextView?.text = body
            viewedCheckmarkImageView?.visibility = View.INVISIBLE
        }
    }
}