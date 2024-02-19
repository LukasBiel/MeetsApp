package com.example.myapplication.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Post
import com.example.myapplication.databinding.PostBinding
import com.example.myapplication.repository.UserRepository


interface OnPostItemClickListener {
    fun onPostItemClick(post: Post)
}

class PostAdapter(private val posts: List<Post>,private val itemClickListener: OnPostItemClickListener) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    // ViewHolder reprezentujący pojedynczy widok elementu listy
    class PostViewHolder(private val binding: PostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Tutaj możesz odwoływać się do elementów UI za pomocą binding
        @SuppressLint("SetTextI18n")
        fun bind(post: Post) {
            binding.titleTextView.text = post.title
            binding.contentTextView.text = post.short_content
            binding.CategorytextView.text = post.kategorie?.get(0)
            binding.LocalizationtextView.text = post.localization
            binding.PeopletextView.text = post.people_number.toString()+"/"+post.max_people.toString()

            val userRepository = UserRepository()
            post.userId?.let {
                userRepository.getUser(it){ user ->
                    if (user != null) {
                        binding.authorTextView.text = user.imie + " " + user.nazwisko
                    } else {
                        binding.authorTextView.text = ""

                    }
                }
            }
        }
    }

    // Metoda wywoływana przy tworzeniu nowego ViewHoldera
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            PostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    // Metoda wywoływana przy przypisywaniu danych do ViewHoldera
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val reversed_position = itemCount - position - 1

        holder.bind(posts[reversed_position])
        holder.itemView.setOnClickListener {
            itemClickListener.onPostItemClick(posts[reversed_position])
        }

    }

    // Metoda zwracająca liczbę elementów na liście
    override fun getItemCount(): Int {
        return posts.size
    }
}
