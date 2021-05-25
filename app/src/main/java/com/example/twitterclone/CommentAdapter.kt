package com.example.twitterclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitterclone.models.Comment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CommentAdapter(options: FirestoreRecyclerOptions<Comment>, private val listener: ICommentAdapter) : FirestoreRecyclerAdapter<Comment, CommentAdapter.CommentViewHolder>(options) {

       class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

                        val cmntuserImage: ImageView = itemView.findViewById(R.id.cmntuserImage)
                        val cmntuserName: TextView = itemView.findViewById(R.id.cmntuserName)
                        val cmntcreatedAt: TextView = itemView.findViewById(R.id.cmntcreatedAt)
                        val cmnttext: TextView = itemView.findViewById(R.id.commenttxt)
                        val cmntlikebtn: ImageView = itemView.findViewById(R.id.cmntlikebtn)
                        val cmntlikecount: TextView = itemView.findViewById(R.id.cmntlikeCount)
       }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {

        val cmntviewHolder = CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_view,parent, false))
        cmntviewHolder.cmntlikebtn.setOnClickListener {
                 listener.onLikeClicked(snapshots.getSnapshot(cmntviewHolder.adapterPosition).id)
        }
        return cmntviewHolder

    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int, model: Comment) {

           Glide.with(holder.cmntuserImage.context).load(model.commenteduser.imageUrl).circleCrop().into(holder.cmntuserImage)
           holder.cmntuserName.text = model.commenteduser.displayName
           holder.cmntcreatedAt.text = Utils.getTimeAgo(model.createdAt)
           holder.cmnttext.text = model.text
           if(model.likedBy.size > 0)
                  holder.cmntlikecount.text = model.likedBy.size.toString()

           val auth = Firebase.auth
           val currentUser = auth.currentUser!!.uid
           val isLiked = model.likedBy.contains(currentUser)

           if(isLiked)
                 holder.cmntlikebtn.setImageDrawable(ContextCompat.getDrawable(holder.cmntlikebtn.context, R.drawable.ic_liked))
           else
                 holder.cmntlikebtn.setImageDrawable(ContextCompat.getDrawable(holder.cmntlikebtn.context, R.drawable.ic_unlike))

    }

}

interface ICommentAdapter {
       fun onLikeClicked(commentId: String)
}