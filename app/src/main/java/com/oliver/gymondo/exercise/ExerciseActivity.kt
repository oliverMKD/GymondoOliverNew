package com.oliver.gymondo.exercise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.oliver.gymondo.R
import com.oliver.gymondo.databinding.ActivityExerciseBinding
import com.oliver.gymondo.exercise.fragments.ListFragment
import com.oliver.gymondo.exercise.utils.inTransaction

class ExerciseActivity :
    AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityExerciseBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_exercise
        )

        // check if activity new instance
        if (savedInstanceState == null) {
            replaceFragment(ListFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment.isAdded) return
        supportFragmentManager.inTransaction {
            replace(R.id.nav_host_fragment, fragment)
        }
    }

    fun addFragment(fragment: Fragment) {
        if (fragment.isAdded) return
        supportFragmentManager.inTransaction {
            setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )

            addToBackStack(fragment::class.java.name)
            add(R.id.nav_host_fragment, fragment)
        }
    }
}
