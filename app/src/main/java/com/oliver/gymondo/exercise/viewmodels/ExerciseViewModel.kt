package com.oliver.gymondo.exercise.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliver.gymondo.database.models.Exercise
import com.oliver.gymondo.exercise.ExerciseActivity
import com.oliver.gymondo.exercise.repositories.GymondoRepository
import kotlinx.coroutines.*

/**
 * The ViewModel for [ExerciseActivity].
 */
class ExerciseViewModel internal constructor(
    private val gymondoRepository: GymondoRepository
) : ViewModel() {

    val exerciseResponse: LiveData<List<Exercise>> = gymondoRepository.exerciseResponse
    val nekojResponse = gymondoRepository.responseExercise
    val exception: MutableLiveData<Exception> = gymondoRepository.exception
    var boolean: MutableLiveData<Boolean> = gymondoRepository.boolean
    val nextPage: MutableLiveData<String> = gymondoRepository.nextPage
    val imageCount: MutableLiveData<Int> = gymondoRepository.imageCount
    val exerciseCount: MutableLiveData<Int> = gymondoRepository.exerciseCount


    fun getExercises() {
        viewModelScope.launch {
            val im = async { gymondoRepository.getImage() }
            if (!im.await().body()!!.next.isNullOrEmpty()) {
                im.await().apply { gymondoRepository.getNextImage(im.await().body()!!.next!!) }
            }
            val one = async { gymondoRepository.getExercises() }
            one.await().let {
                val two = async { gymondoRepository.getCategory() }
                two.await().apply {
                    val three = async { gymondoRepository.getMuscle() }
                    three.await().apply {
                        gymondoRepository.getEquipment()
                    }
                }
            }
            withContext(Dispatchers.Main) {
                boolean.value = true
            }
        }
    }

    suspend fun getNexExercises(next: String) {
        gymondoRepository.getNextExercise(next)
        withContext(Dispatchers.Main) {
            boolean.value = true
        }
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}