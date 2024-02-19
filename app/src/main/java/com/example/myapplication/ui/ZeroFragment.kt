package com.example.myapplication.ui

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.FragmentZeroBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ZeroFragment : Fragment(){
    private var _binding: FragmentZeroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentZeroBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        //bottomNavigationView.visibility = View.GONE

        binding.zaloguj.setOnClickListener {
                (activity as? MainActivity<*>)?.signIn(
                    binding.editTextTextEmailAddress2.text.toString(),
                    binding.editTextTextPassword2.text.toString()
                )
            }

        binding.Zarejestruj.setOnClickListener {
            findNavController().navigate(R.id.action_zeroFragment_to_registerationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}