package com.example.twitterclone.daos

import com.example.twitterclone.models.Comment
import com.example.twitterclone.models.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CommentDao {

    val db = FirebaseFirestore.getInstance()
    val cmntCollection = db.collection("comments")
    private val auth = Firebase.auth
    private val currentUser = auth.currentUser!!.uid

    fun addComment(postId: String, text: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val userDao = UserDao()
            val postDao = PostDao()

            val user = userDao.getUserById(currentUser).await().toObject(com.example.twitterclone.models.User::class.java)!!
            val post = postDao.getPostById(postId).await().toObject(Post::class.java)
            val pid = postId.trim()
            val createdAt = System.currentTimeMillis()

            val comment = post?.let { Comment(text,user,it,pid,createdAt) }

            if (comment != null) {
                cmntCollection.document().set(comment)
                postDao.updateComments(postId)
            }


        }
    }


    fun getCommentById(commentId: String): Task<DocumentSnapshot> {
        return cmntCollection.document(commentId).get()
    }


    fun updateLikes(commentId: String) {

        GlobalScope.launch {

            val comment = getCommentById(commentId).await().toObject(Comment::class.java)
            val isLiked = comment!!.likedBy.contains(currentUser)

            if(isLiked)
                comment.likedBy.remove(currentUser)
            else
                comment.likedBy.add(currentUser)

            cmntCollection.document(commentId).set(comment)

        }
    }

}