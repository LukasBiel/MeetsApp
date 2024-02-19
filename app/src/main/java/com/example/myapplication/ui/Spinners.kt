package com.example.myapplication.ui

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.AdapterView

class SpinnerManager {
    fun setupSpinner1(spinner: Spinner) {

        val adapter = ArrayAdapter<String>(
            spinner.context,
            android.R.layout.simple_spinner_item,
            listOf("Mężczyzna", "Kobieta", "Inne/Nie chcę wybierać")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle item selection for Spinner 1cla
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected for Spinner 1
            }
        }
    }
}