package com.example.myapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSecondBinding
import com.example.myapplication.repository.PostRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.GONE

        val tags = mutableListOf<String>()
        val kategorie = mutableListOf<String>()

        try {
            val inputStream: InputStream = resources.openRawResource(R.raw.tagi_kategorie)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                val tagi = line?.split(",") ?: emptyList()
                tags.addAll(tagi)
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, tags)
        val autoCompleteTextView = binding.editKategoria
        autoCompleteTextView.setAdapter(adapter)

        binding.imageButton.setOnClickListener {
            if (kategorie.size<3){
                val selectedTag = autoCompleteTextView.text.toString()
                kategorie.add(selectedTag)
                autoCompleteTextView.text = null
            }

        }
        binding.dodaj.setOnClickListener {

            val max_osob = binding.editTextText.text.toString().toIntOrNull() ?: 0
            val title = binding.editTextText2.text.toString()
            val opis = binding.content.text.toString()
            val short_content = binding.shortContent.text.toString()
            val localization = binding.editLokalizacja.text.toString()

            if (CheckRules(max_osob,kategorie, opis, short_content, localization, title)){
                post_database(max_osob, kategorie, opis, short_content, localization, title)
                post_ui()
            }



            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }
    private  fun CheckRules(max_osob: Int, Kategorie: MutableList<String>, opis:String, short_content:String, localization:String, title:String): Boolean {
        val a: Boolean
        if(title.length>0){
            a = true
        }else{
            a = false
            binding.inputlayout.error = "Pole nie może być puste"
        }
        val b: Boolean
        if(opis.length>=200){
            b = true
        }else{
            b = false
            binding.inputlayoutopis.error = "Zbyt krótki opis"
        }
        val c: Boolean
        if(short_content.length>0){
            c = true
        }else{
            c = false
            binding.inputlayoutSopis.error = "Pole nie może być puste"
        }
        val d: Boolean
        if(max_osob in 1..99){
            d = true
        }else{
            d = false
            binding.inputlayoutSopis.error = "Niepoprawna ilość osób"
        }
        if (a && b && c && d){
            return true
        }else{
            return false
        }
    }
    private fun post_database(max_osob: Int, kategorie: MutableList<String>, opis:String, short_content:String, localization:String, title:String){
        val postRepository = PostRepository()
        postRepository.addPost(title, short_content, opis,  max_osob, kategorie, localization)
    }
    private fun post_ui(){

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}