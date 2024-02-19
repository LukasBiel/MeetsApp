package com.example.myapplication.repository

import com.example.myapplication.data.Post
import com.example.myapplication.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostRepository {

    private val postsRef: DatabaseReference =
        FirebaseDatabase.getInstance("https://meets-ab1ca-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("posts")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun get_latest_posts(limit: Int, callback: (List<Post>) -> Unit) {
        val query = FirebaseDatabase.getInstance().getReference("posts")
            .orderByChild("timestamp")
            .limitToLast(limit)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val posts = mutableListOf<Post>()
                    for (postSnapshot in dataSnapshot.children) {
                        val post = postSnapshot.getValue(Post::class.java)
                        post?.let { posts.add(it) }

                    }
                    callback(posts)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(emptyList())
            }
        })

    }


    fun addPost(
        title: String,
        short_content: String,
        content: String,
        max_people: Int,
        kategorie: MutableList<String>,
        localization: String
    ) {
        val currentUser: FirebaseUser? = auth.currentUser
        val userId: String? = currentUser?.uid

        val UserRepository = UserRepository()

        if (userId != null) {
            UserRepository.NewPost(userId)
            UserRepository.getUser(userId) { user ->
                if (user != null) {

                    val postId = postsRef.push().key
                    val timestamp = System.currentTimeMillis()
                    val post = Post(
                        postId,
                        userId,
                        title,
                        short_content,
                        content,
                        kategorie,
                        0,
                        max_people,
                        localization,
                        timestamp
                    )

                    postId?.let {
                        postsRef.child(it).setValue(post)
                    }
                } else {

                }
            }
        }



        fun getPosts(): DatabaseReference {
            return postsRef
        }
    }
}

