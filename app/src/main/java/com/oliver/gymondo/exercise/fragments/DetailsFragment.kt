package com.oliver.gymondo.exercise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oliver.gymondo.database.GymondoDatabase
import com.oliver.gymondo.databinding.FragmentDetailsBinding
import com.oliver.gymondo.exercise.adapters.ListAdapter
import com.oliver.gymondo.exercise.components.DaggerExerciseComponent
import com.oliver.gymondo.exercise.modules.ExerciseModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentDetailsBinding
    private var id: Int? = null
    lateinit var gymondoDatabase: GymondoDatabase


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        context ?: return binding.root
        DaggerExerciseComponent.builder().exerciseModule(ExerciseModule()).build().inject(this)
        gymondoDatabase = GymondoDatabase.getInstance(context!!)
        id = arguments?.getInt(DETAILS_FRAGMENT_ID)
        val adapter = ListAdapter()
        binding.recyclerView.adapter = adapter

        getData(id!!, adapter)
        return binding.root
    }

    private fun getData(id: Int, adapter: ListAdapter) {
        CoroutineScope(Dispatchers.IO).launch {
            val exercise = gymondoDatabase.exerciseDao().searchById(id)
            binding.detailsItemExercise = exercise
            withContext(Dispatchers.Main) {
                adapter.submitList(exercise.images!!)
            }
        }
    }

    companion object {
        private const val DETAILS_FRAGMENT_ID =
            "DETAILS_FRAGMENT_ID"

        fun newInstance(id: Int) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(DETAILS_FRAGMENT_ID, id)
            }
        }
    }
}