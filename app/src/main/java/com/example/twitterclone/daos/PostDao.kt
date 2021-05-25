package com.example.twitterclone.daos

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.twitterclone.CommentActivity
import com.example.twitterclone.PostActivity
import com.example.twitterclone.models.Comment
import com.example.twitterclone.models.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.platforminfo.GlobalLibraryVersionRegistrar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.Serializable

class PostDao {
      val db = FirebaseFirestore.getInstance()
      val postCollection = db.collection("posts")
      private val auth = Firebase.auth
      private val currentUser = auth.currentUser!!.uid

      fun addPost(text: String) {

          GlobalScope.launch(Dispatchers.IO) {
                    val userDao = UserDao()
                    val user = userDao.getUserById(currentUser).await().toObject(com.example.twitterclone.models.User::class.java)!!

                    val createdAt = System.currentTimeMillis()

                    val postedName = auth.currentUser!!.displayName!!.trim()
                    val postedImage = auth.currentUser!!.photoUrl.toString().trim()

                    val post = Post(text, user, postedName, postedImage, createdAt)
                    postCollection.document().set(post)
             }

      }

      fun retweet(postId: String) {

      }

      fun getPostById(postId: String): Task<DocumentSnapshot>  {
               return postCollection.document(postId).get()
      }


      fun updateLikes(postId: String) {

          GlobalScope.launch {
 
                val post = getPostById(postId).await().toObject(Post::class.java)
                val isLiked = post!!.likedBy.contains(currentUser)

                if(isLiked)
                       post.likedBy.remove(currentUser)
                else
                     post.likedBy.add(currentUser)

                postCollection.document(postId).set(post)

          }
      }

      fun updateComments(postId: String) {

          GlobalScope.launch {

              val post = getPostById(postId).await().toObject(Post::class.java)
              post!!.commentedBy.add(currentUser)

              postCollection.document(postId).set(post)

          }

      }

}