package com.example.twitterclone.models

data class Post (
     val text: String = "",
     val createdBy: User = User(),
     val creatorName: String = "",
     val creatorImage: String = "",
     val createdAt: Long = 0L,
     val likedBy: ArrayList<String> = ArrayList(),
     val commentedBy: ArrayList<String> = ArrayList()
)