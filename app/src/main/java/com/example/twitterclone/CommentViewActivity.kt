package com.example.twitterclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.twitterclone.daos.CommentDao
import com.example.twitterclone.daos.PostDao
import com.example.twitterclone.databinding.ActivityCommentViewBinding
import com.example.twitterclone.databinding.ActivityMainBinding
import com.example.twitterclone.models.Comment
import com.example.twitterclone.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.w3c.dom.Text
import java.util.ArrayList

class CommentViewActivity : AppCompatActivity(), ICommentAdapter {

    private lateinit var adapter: CommentAdapter
    private lateinit var commentDao: CommentDao
    private lateinit var binding: ActivityCommentViewBinding


    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_view)

        binding = ActivityCommentViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val pId = intent.getStringExtra("postId")!!.trim()

        val postDao: PostDao = PostDao()
        val postCollection = postDao.postCollection.document(pId)

        val userImage: ImageView = findViewById(R.id.userImage)
        val userName: TextView = findViewById(R.id.userName)
        val createdAt: TextView = findViewById(R.id.createdAt)
        val postText: TextView = findViewById(R.id.postTitle)
        val likecount: TextView = findViewById(R.id.likeCount)
        val commentCount: TextView = findViewById(R.id.cmntCount)


        postCollection.get().addOnSuccessListener(OnSuccessListener {
                      postText.text = it.getString("text")
                      createdAt.text = it.getLong("createdAt")?.let { it1 -> Utils.getTimeAgo(it1) }
                      userName.text = it.getString("creatorName")
                      Glide.with(userImage.context).load(it.getString("creatorImage")).circleCrop().into(userImage)


        })
                .addOnFailureListener(OnFailureListener {

                })

       setUpRecyclerView()

    }

    private fun setUpRecyclerView() {

             val postId = intent.getStringExtra("postId")!!.trim()

             commentDao = CommentDao()
             val commentCollection = commentDao.cmntCollection.whereEqualTo("postId",postId)

             val query = commentCollection.orderBy("createdAt",Query.Direction.DESCENDING)

             val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Comment>().setQuery(query, Comment::class.java).build()

             adapter = CommentAdapter(recyclerViewOptions,this)

             binding.cmntrecyclerview.adapter = adapter
             binding.cmntrecyclerview.layoutManager = LinearLayoutManager(this)

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(commentId: String) {
               commentDao.updateLikes(commentId)
    }


}

