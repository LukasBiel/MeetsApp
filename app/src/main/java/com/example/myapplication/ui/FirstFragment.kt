package com.example.myapplication.ui

import android.content.Context
import com.example.myapplication.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.Post
import com.example.myapplication.databinding.FragmentFirstBinding
import com.example.myapplication.repository.PostRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class CustomSpinnerAdapter(context: Context, items: List<String>) :
    ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {

    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        return view
    }
}

class FirstFragment : Fragment(),OnPostItemClickListener {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(showUpArrow())

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.VISIBLE

        val postRepository = PostRepository()
        postRepository.get_latest_posts(10){posts ->
            val adapter = PostAdapter(posts, this)
            val spaceInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())
            binding.recycler.addItemDecoration(ItemDecoration(spaceInPixels))
            binding.recycler.adapter = adapter

        }


        val customEntries = listOf("Sport", "Bar", "Sztuka", "Gry planszowe")
        // Dodaj więcej elementów, jeśli to konieczne

        // Inicjalizacja Spinera
        val spinner: Spinner = view.findViewById(R.id.spinner2)
        val adapter = CustomSpinnerAdapter(requireContext(), customEntries)
        spinner.adapter = adapter



    }
    override fun onPostItemClick(post: Post) {
        val action = FirstFragmentDirections.actionFirstFragmentToPostFragment(post)
        findNavController().navigate(action)
    }
    private fun showUpArrow(): Boolean {

        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}