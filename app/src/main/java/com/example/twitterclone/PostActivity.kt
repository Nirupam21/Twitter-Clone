package com.example.twitterclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.twitterclone.daos.PostDao
import com.example.twitterclone.databinding.ActivityPostBinding
import com.example.twitterclone.databinding.ActivitySignInBinding

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {

        postDao = PostDao()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        binding = ActivityPostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tweetbtn.setOnClickListener {
                val input = binding.postLayout.text.toString().trim()

                if(input.isNotEmpty()) {
                         postDao.addPost(input)

                }
                finish()
        }

    }
}