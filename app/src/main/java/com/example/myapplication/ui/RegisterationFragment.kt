package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.RegistrationFragmentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar
import java.util.GregorianCalendar

class RegisterationFragment: Fragment() {
    private var _binding: RegistrationFragmentBinding? = null
    private val binding get() = _binding!!
    private val bindingAdapter = BindingAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        getActivity()?.setTitle("My title")

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        //bottomNavigationView.visibility = View.GONE

        val spinner1: Spinner = binding.spinnerPlec
        val SpinnerManager = SpinnerManager()
        SpinnerManager.setupSpinner1(spinner1)

        binding.createAccount.setOnClickListener {
            val email  = binding.editTextTextEmailAddress.text.toString()
            val password =  binding.editTextTextPassword.text.toString()
            val imie = binding.imieId.text.toString()
            val nazwisko = binding.nazwiskoId.text.toString()
            val dzien = binding.dayId.text.toString().toIntOrNull()
            val miesiac = binding.monthId.text.toString().toIntOrNull()
            val rok = binding.yearId.text.toString().toIntOrNull()
            val plec = binding.spinnerPlec.getItemAtPosition(binding.spinnerPlec.selectedItemPosition).toString()
            if(CheckRules(imie,nazwisko,dzien, miesiac, rok, password, email)){
                if (rok!=null){
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                    val age = currentYear - rok
                    (activity as? MainActivity<*>)?.createAccount(email, password, imie, nazwisko, age, plec )
                }


            }



        }

    }
    private fun CheckRules(imie:String, nazwisko:String, dzien:Int?, miesiac:Int?, rok:Int?, haslo: String,email:String): Boolean {
        ClearErrors()
        val a = IsNameValid(imie)
        val b = IsSurnameValid(nazwisko)
        val c = IsDateValid(dzien,miesiac,rok)
        val d = IsAdult(rok)
        val e = CheckPassword(haslo)
        val f = CheckEmail(email)
        if (a && b && c && d && e && f){
            return true
        }else{
            return false
        }
    }
    private fun IsNameValid(imie:String ): Boolean {
        return if (imie != "") {
            if (imie.length<2){
                binding.name.error = "Za krótkie imię"
                false}

            else{
                true
            }

        }else{
            binding.name.error = "Pole nie może być puste"
            false
        }
    }
    private fun IsSurnameValid(nazwisko:String): Boolean {
        return if (nazwisko != "") {
            if (nazwisko.length<2){
                binding.surname.error = "Za krótkie nazwisko"
                false
            }else{
                true
            }
        }else{
            binding.surname.error = "Pole nie może być puste"
            false
        }
    }

    private fun IsDateValid(dzien: Int?, miesiac: Int?, rok: Int?): Boolean {
        if(dzien!=null && miesiac!=null && rok!=null)
        {
            try {

                val calendar = GregorianCalendar()
                calendar.setLenient(false) // Ensure strict validation

                // Set the calendar to the specified date
                calendar.set(Calendar.YEAR, rok)
                calendar.set(Calendar.MONTH, miesiac - 1) // Months are 0-based
                calendar.set(Calendar.DAY_OF_MONTH, dzien)

                // Attempt to perform a date calculation (this will throw an exception if the date is invalid)
                calendar.timeInMillis

                return true
            } catch (e: Exception) {
                binding.errorDate.visibility = View.VISIBLE
                binding.errorDate.text = "Nieprawidłowa data"
                return false
            }
        }else{
            binding.errorDate.visibility = View.VISIBLE
            binding.errorDate.text = "Nieprawidłowa data"
            return false
        }

    }
    private fun IsAdult(rok: Int?): Boolean {
        if (rok!=null){
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val age = currentYear - rok

        if(age < 18){
            binding.errorDate.visibility = View.VISIBLE
            binding.errorDate.text = "Osoba musi być pełnoletnia"
            return false
        }else if (age>124){
            binding.errorDate.visibility = View.VISIBLE
            binding.errorDate.text = "Nieprawidłowa data"
            return false
        }else{
            return true
        }
        }else {
            binding.errorDate.visibility = View.VISIBLE
            binding.errorDate.text = "Nieprawidłowa data"
            return false
        }
    }


    private fun CheckPassword(haslo : String): Boolean {

        if (haslo.length >= 6) {
            if (haslo.none { it.isLetter() }){
                binding.errorHaslo.visibility = View.VISIBLE
                binding.errorHaslo.text = "Hasło musi zawierać literę"
                return false

            }else if (haslo.none { it.isUpperCase() }){
                binding.errorHaslo.visibility = View.VISIBLE
                binding.errorHaslo.text = "Hasło musi zawierać wielką literę"
                return false
            }
            else if (haslo.none{it.isDigit()}){
                binding.errorHaslo.visibility = View.VISIBLE
                binding.errorHaslo.text = "Hasło musi zawierać liczbę"
                return false
            }else if (haslo.none{it.isSpecialChar()}){
                binding.errorHaslo.visibility = View.VISIBLE
                binding.errorHaslo.text = "Hasło musi zawierać znak specjalny"
                return false

            }else if (haslo.any{it.isWhitespace()}){
                binding.errorHaslo.visibility = View.VISIBLE
                binding.errorHaslo.text = "Hasło nie może zawierać spacji"
                return false
            }
            else{
                return true
            }


        }else{
            binding.errorHaslo.visibility = View.VISIBLE
            binding.errorHaslo.text = "Hasło musi mieć przynajmniej 6 znaków"
            return false
        }

    }

    private fun CheckEmail(email: String):Boolean{
        return if (email.isEmpty()){
            false
        } else{
            true
        }
    }
    fun Char.isSpecialChar(): Boolean {
        return "!@#$%^&*()_-+=<>?.,/".contains(this)
    }
    private fun ClearErrors(){
        binding.errorDate.visibility = View.GONE
        binding.errorHaslo.visibility = View.GONE
        binding.day.error = null
        binding.month.error = null
        binding.year.error = null
        binding.name.error = null
        binding.surname.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}