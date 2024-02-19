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

class UserRepository {

    private val usersRef: DatabaseReference = FirebaseDatabase.getInstance("https://meets-ab1ca-default-rtdb.europe-west1.firebasedatabase.app").getReference("users")

    fun NewPost(userId: String) {
        val userRef = usersRef.child(userId)
        userRef.child("ile_postow").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentValue = snapshot.getValue(Int::class.java) ?: 0
                // Increment the posts_number field by 1
                userRef.child("ile_postow").setValue(currentValue + 1)
                    .addOnSuccessListener {
                        // Successfully incremented posts_number
                    }
                    .addOnFailureListener { e ->
                        // Handle failure
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }


    fun addUser(userId:String, imie:String, nazwisko:String, wiek: Int, plec: String ) {

        userId.let {

            val user = User(userId, imie, nazwisko, wiek, plec, 0 )
            usersRef.child(it).setValue(user)

        }
    }

    fun getUser(userId: String, callback: (User?) -> Unit) {
        usersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: User? = snapshot.getValue(User::class.java)
                callback(user)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                callback(null)
            }
        })
    }

}