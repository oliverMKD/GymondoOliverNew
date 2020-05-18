package com.oliver.gymondo.exercise.components

import com.oliver.gymondo.exercise.fragments.DetailsFragment
import com.oliver.gymondo.exercise.fragments.ListFragment
import com.oliver.gymondo.exercise.modules.ExerciseModule
import com.oliver.gymondo.exercise.viewmodels.ExerciseViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ExerciseModule::class])
interface ExerciseComponent {

    fun exerciseViewModelFactory(): ExerciseViewModelFactory

    fun inject(fragment: DetailsFragment)

    fun inject(fragment: ListFragment)
}