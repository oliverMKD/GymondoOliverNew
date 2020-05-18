package com.oliver.gymondo.exercise.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliver.gymondo.database.models.Exercise
import com.oliver.gymondo.exercise.ExerciseActivity
import com.oliver.gymondo.exercise.repositories.GymondoRepository
import com.oliver.gymondo.network.responses.ExerciseResponse
import kotlinx.coroutines.*
import retrofit2.Response

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
    var page = 1
    var exResponse: ExerciseResponse? = null


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

    suspend fun getNexExercises(next: String) : Response<ExerciseResponse> {
      val response=  gymondoRepository.getNextExercise(next)
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                if(exResponse == null) {
                    exResponse = resultResponse
                } else {
                    val oldArticles = exResponse?.results
                    val newArticles = resultResponse.results
                    oldArticles?.addAll(newArticles!!)
                }
            }
        }
        withContext(Dispatchers.Main) {
            boolean.value = true
        }
        return gymondoRepository.getNextExercise(next)
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}