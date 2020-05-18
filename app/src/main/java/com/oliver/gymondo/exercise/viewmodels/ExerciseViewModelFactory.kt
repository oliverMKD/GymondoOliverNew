package com.oliver.gymondo.exercise.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oliver.gymondo.exercise.repositories.GymondoRepository

/**
 * Factory for creating a [ExerciseViewModel] with a constructor that takes a [GymondoRepository].
 */
class ExerciseViewModelFactory(
    private val gymondoRepository: GymondoRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ExerciseViewModel(gymondoRepository) as T
}