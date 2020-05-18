package com.oliver.gymondo.exercise.modules

import com.oliver.gymondo.exercise.repositories.GymondoRepository
import com.oliver.gymondo.exercise.viewmodels.ExerciseViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ExerciseModule {

    @Provides
    @Singleton
    fun providesExerciseViewModelFactory(gymondoRepository: GymondoRepository): ExerciseViewModelFactory {
        return ExerciseViewModelFactory(gymondoRepository)
    }

    @Provides
    @Singleton
    fun gymondoRepository(): GymondoRepository = GymondoRepository()

}