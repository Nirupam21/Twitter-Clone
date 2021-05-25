package com.example.twitterclone


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twitterclone.daos.PostDao
import com.example.twitterclone.daos.UserDao
import com.example.twitterclone.databinding.ActivityMainBinding
import com.example.twitterclone.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), IPostAdapter {

    private lateinit var postDao: PostDao
    private lateinit var adapter: PostAdapter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.floatingbtn.setOnClickListener {
                val intent = Intent(this,PostActivity::class.java)
                startActivity(intent)
        }

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {

          postDao = PostDao()
          val postCollections = postDao.postCollection
          val query = postCollections.orderBy("createdAt", Query.Direction.DESCENDING)
          val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()

          adapter = PostAdapter(recyclerViewOptions, this)

          binding.recyclerview.adapter = adapter
          binding.recyclerview.layoutManager = LinearLayoutManager(this)

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
              postDao.updateLikes(postId)
    }

    override fun onCommentClicked(postId: String) {

        val text = postId.trim()

        val intent = Intent(this, CommentActivity::class.java).apply {
                putExtra("postId",text)
        }

        startActivity(intent)

    }

    override fun onPostClicked(postId: String) {
        val text = postId.trim()

        val intent = Intent(this, CommentViewActivity::class.java).apply {
            putExtra("postId",text)
        }

        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.logout -> {
                Firebase.auth.signOut()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

}
