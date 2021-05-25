package com.example.twitterclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.twitterclone.daos.CommentDao
import com.example.twitterclone.daos.PostDao
import com.example.twitterclone.databinding.ActivityCommentBinding
import com.example.twitterclone.databinding.ActivityPostBinding

class CommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding
    private lateinit var commentDao: CommentDao

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        commentDao = CommentDao()

        binding = ActivityCommentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        binding.buttonPanel.setOnClickListener {

                 val input = binding.comment.text.toString().trim()
                 val intent: Intent = intent


                 if(input.isNotEmpty()) {
                     val postId = intent.getStringExtra("postId")

                     if (postId != null) {
                         //Log.d("got intent data", postId)
                         commentDao.addComment(postId, input)
                     }
                 }
                 finish()

        }

    }
}