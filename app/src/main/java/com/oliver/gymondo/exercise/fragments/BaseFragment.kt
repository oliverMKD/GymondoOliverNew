package com.oliver.gymondo.exercise.fragments

import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    fun handleNetworkError(exception: Exception) {
        Toast.makeText(activity, exception.localizedMessage, Toast.LENGTH_SHORT).show()
    }
}