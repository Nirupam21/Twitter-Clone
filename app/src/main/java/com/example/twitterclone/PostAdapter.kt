package com.example.twitterclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitterclone.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(options: FirestoreRecyclerOptions<Post>, private val listener:IPostAdapter) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(options){

    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val post: TextView = itemView.findViewById(R.id.postTitle)
        val username: TextView = itemView.findViewById(R.id.userName)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val commentCount: TextView = itemView.findViewById(R.id.commentCount)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
        val userImage: ImageView = itemView.findViewById(R.id.userImage) 
        val commentbtn: ImageView = itemView.findViewById(R.id.comment)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val viewHolder =  PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_view,parent,false))
        viewHolder.likeButton.setOnClickListener {

                listener.onLikeClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }

        viewHolder.commentbtn.setOnClickListener {

                listener.onCommentClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }

        viewHolder.post.setOnClickListener {

                listener.onPostClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }


        return viewHolder

    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {

        holder.post.text = model.text
        holder.username.text = model.createdBy.displayName
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)
        if(model.likedBy.size > 0)
                holder.likeCount.text = model.likedBy.size.toString()
        if(model.commentedBy.size > 0)
                holder.commentCount.text = model.commentedBy.size.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)
        val auth = Firebase.auth
        val currentUser = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUser)
        if(isLiked)
             holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_liked))
        else
             holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_unlike))

    }

}

interface IPostAdapter {
     fun onLikeClicked(postId: String)

     fun onCommentClicked(postId: String)

     fun onPostClicked(postId: String)

}