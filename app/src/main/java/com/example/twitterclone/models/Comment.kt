package com.example.twitterclone.models

data class Comment (

        val text: String = "",
        val commenteduser: User = User(),
        val commentedPost: Post = Post(),
        val postId: String = "",
        val createdAt: Long = 0L,
        val likedBy: ArrayList<String> = ArrayList(),
        val repliedBy: ArrayList<String> = ArrayList()
)