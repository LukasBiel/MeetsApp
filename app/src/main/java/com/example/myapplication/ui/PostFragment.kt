package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.myapplication.data.Post
import com.example.myapplication.databinding.FragmentFirstBinding
import com.example.myapplication.databinding.FragmentPostBinding
import com.example.myapplication.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class PostFragment: Fragment(){
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser: FirebaseUser? = auth.currentUser
        val userId: String? = currentUser?.uid

        val args: PostFragmentArgs by navArgs()
        val post: Post = args.selectedPost

        if (post.userId == userId){

            binding.dolacz.visibility = View.GONE
            binding.priv.visibility = View.GONE
        }else{
            binding.edytuj.visibility = View.GONE

        }
        binding.tytul.text = post.title
        binding.contentt.text = post.content
        binding.kategoria1.text = post.kategorie?.get(0)
        binding.kategoria2.text = post.kategorie?.get(1)
        binding.kategoria3.text = post.kategorie?.get(2)
        val userRepository = UserRepository()
        post.userId?.let {
            userRepository.getUser(it) { user ->
                if (user != null) {
                    binding.imienazwisko.text = user.imie + " " + user.nazwisko
                    binding.wiekoautorze.text = "Wiek: " + user.wiek.toString()
                    binding.Meetsy.text = "Meetsy: " + "0"
                    binding.ilepostow.text = "Stworzone posty: " + user.ile_postow.toString()
                } else {
                    binding.imienazwisko.text = ""
                    binding.wiekoautorze.text = "Wiek: "
                    binding.Meetsy.text = "Meetsy: "
                    binding.ilepostow.text = "Stworzone posty: "
                }
            }
            //binding.wiekoautorze.text ="Wiek: " + post.wiek.toString()

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}