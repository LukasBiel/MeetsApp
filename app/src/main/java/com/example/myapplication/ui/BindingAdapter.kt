package com.example.myapplication.ui

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.BindingAdapter


class BindingAdapter {
    companion object {
        @BindingAdapter("hideKeyboardOnTouch")
        @JvmStatic
        fun View.hideKeyboardOnTouch(editText: EditText) {
            setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val location = IntArray(2)
                    editText.getLocationOnScreen(location)

                    val x = event.rawX + editText.left - location[0]
                    val y = event.rawY + editText.top - location[1]

                    if (x < editText.left || x > editText.right || y < editText.top || y > editText.bottom) {
                        hideKeyboard(editText)
                    }
                }
                false
            }
        }

        private fun hideKeyboard(editText: EditText) {
            val imm = editText.context.getSystemService(InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(editText.windowToken, 0)
        }
    }
}