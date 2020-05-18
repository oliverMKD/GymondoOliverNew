package com.oliver.gymondo.exercise.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.oliver.gymondo.app.GymondoApp
import com.oliver.gymondo.database.GymondoDatabase
import com.oliver.gymondo.database.models.*
import com.oliver.gymondo.network.GymondoApiService
import com.oliver.gymondo.network.responses.ExerciseImageResponse
import com.oliver.gymondo.network.responses.ExerciseResponse
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject

class GymondoRepository {

    @Inject
    lateinit var gymondoApiService: GymondoApiService

    @Inject
    lateinit var gymondoDatabase: GymondoDatabase

    init {
        GymondoApp.instance.getAppComponent().inject(this)
    }

    val exerciseResponse: LiveData<List<Exercise>> = gymondoDatabase.exerciseDao().getAllExercise()

    val responseExercise: LiveData<PagedList<ModelExercise>> =
        gymondoDatabase.exerciseDao().getAllNekoj().toLiveData(20)

    val exception = MutableLiveData<Exception>()
    val boolean = MutableLiveData<Boolean>()
    val nextPage = MutableLiveData<String>()
    private val newMuscleList: MutableList<Muscle> = mutableListOf()
    private val newEqList: MutableList<Equipment> = mutableListOf()
    private val newImageList: MutableList<ExerciseImage> = mutableListOf()

    suspend fun getExercises(): Response<ExerciseResponse> {
        val response = gymondoApiService.getExercises()
        withContext(Dispatchers.IO) {
            try {
                Log.d("bbbb", "pocna exercise")
                if (response!!.isSuccessful) {
                    for (exercise in response.body()!!.results!!) {
                        exercise?.let {
                            gymondoDatabase.exerciseDao().insertExercise(exercise)
                            gymondoDatabase.exerciseDao()
                                .insertNekoj(
                                    ModelExercise(
                                        exercise.id,
                                        exercise.description,
                                        exercise.name,
                                        null
                                    )
                                )
                            withContext(Dispatchers.Main) {
                                boolean.value = false
                                nextPage.value = response.body()?.next
                            }
                            Log.d("bbbb", "pomina exercise")
                        }
                    }
                } else {
                    Log.d("bbbb", "exercise ne e successful")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("bbbb", "first exer" + e)
                withContext(Dispatchers.Main) {
                    exception.value = e
                }
            }
        }
        return response!!
    }

    suspend fun getCategory() {
        withContext(Dispatchers.IO) {
            Log.d("bbbb", "category pocinja")
            try {
                val category: MutableList<Muscle> = mutableListOf()
                val categoryResponse = gymondoApiService.getCategory()
                if (categoryResponse!!.isSuccessful) {
                    Log.d("bbbb", "catwgory isSuccessful")
                    for (exCategory in categoryResponse.body()!!.results!!) {
                        categoryResponse?.let {
                            gymondoDatabase.exerciseDao().insertCategory(exCategory)
                            val exList = gymondoDatabase.exerciseDao().getExerciseCategory()
                            for (item in exList) {
                                if (item.category == exCategory.category_id) {
                                    gymondoDatabase.exerciseDao()
                                        .insertNekoj(
                                            ModelExercise(
                                                item.id,
                                                item.description,
                                                item.name,
                                                exCategory
                                            )
                                        )
                                }
                            }
                            withContext(Dispatchers.Main) {
                                boolean.value = false
                            }
                        }
                    }
                } else {
                    Log.d("bbbb", "category ne e successful")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("bbbb", "category" + e)
                withContext(Dispatchers.Main) {
                    exception.value = e
                }
            }
        }
    }


    suspend fun getMuscle() {
        withContext(Dispatchers.IO) {
            Log.d("bbbb", "muscle pocinja")
            try {

                val muscleResponse = gymondoApiService.getMuscle()
                if (muscleResponse!!.isSuccessful) {
                    Log.d("bbbb", "muscle isSuccessful")
                    for (muscle in muscleResponse.body()!!.results!!) {
                        muscle?.let {
                            gymondoDatabase.exerciseDao().insertMuscle(muscle)
                            val exList = gymondoDatabase.exerciseDao().getExerciseCategory()
                            val nekoj = gymondoDatabase.exerciseDao().getAllNekojList()
                            for (item in exList) {
                                if (!item.muscles.isNullOrEmpty()) {
                                    for (i in item.muscles) {
                                        if (i == muscle.id) {
                                            newMuscleList.add(muscle)
                                            if (newMuscleList.size == item.muscles.size) {
                                                gymondoDatabase.exerciseDao().updateTestOnly(
                                                    newMuscleList, item.description!!
                                                )
                                                newMuscleList.clear()
                                                break

                                            }


                                        }
                                    }
                                }
                            }
                            withContext(Dispatchers.Main) {
                                boolean.value = false
                            }
                        }
                    }
                } else {
                    Log.d("bbbb", "muscle ne e successful")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("bbbb", "muscle" + e)
                withContext(Dispatchers.Main) {
                    exception.value = e
                }
            }
        }
    }

    suspend fun getEquipment() {
        withContext(Dispatchers.IO) {
            Log.d("bbbb", "getEquipment pocinja")
            try {
                val exList = gymondoDatabase.exerciseDao().getExerciseCategory()
                val equipmentResponse = gymondoApiService.getEquipment()
                if (equipmentResponse!!.isSuccessful) {
                    for (item in exList) {
                        if (!item.equipment.isNullOrEmpty()) {
                            for (i in item.equipment) {
                                Log.d("bbbb", "getEquipment isSuccessful")
                                for (equipment in equipmentResponse.body()!!.results!!) {
                                    equipment?.let {
                                        gymondoDatabase.exerciseDao().insertEquipment(equipment)
                                        if (i == equipment.id) {
                                            newEqList.add(equipment)
                                            if (newEqList.size == item.equipment.size) {
                                                gymondoDatabase.exerciseDao().updateEquipment(
                                                    newEqList, item.description!!
                                                )
                                                newEqList.clear()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    val imageList = gymondoDatabase.exerciseDao().getImageList()
                    val modelList = gymondoDatabase.exerciseDao().getAllNekojList()
                    for (i in modelList) {
                        for (a in imageList) {
                            if (i.id == a.exercise) {
                                newImageList.add(a)
                                gymondoDatabase.exerciseDao()
                                    .updateImages(newImageList, i.description!!)
                                newImageList.clear()
                            }
                        }

                    }
                    withContext(Dispatchers.Main) {
                        boolean.value = false
                    }

                } else {
                    Log.d("bbbb", "getEquipment ne e successful")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("bbbb", "getEquipment" + e)
                withContext(Dispatchers.Main) {
                    exception.value = e
                }
            }
        }
    }

    suspend fun getImage(): Response<ExerciseImageResponse> {
        val response = gymondoApiService.getImage()
        withContext(Dispatchers.IO) {
            try {
                Log.d("bbbb", "image pocinja")
                if (response!!.isSuccessful) {
                    Log.d("bbbb", "image isSuccessful")
                    for (exercise in response.body()!!.results!!) {
                        exercise?.let {
                            gymondoDatabase.exerciseDao().insertImage(exercise)
                        }
                    }
                } else {
                    Log.d("bbbb", "image ne e successful")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("bbbb", "prv image" + e)
                withContext(Dispatchers.Main) {
                    exception.value = e
                }
            }
        }
        return response!!
    }

    suspend fun getNextImage(next: String): Response<ExerciseImageResponse> {
        val response = gymondoApiService.getNextImage(next)
        withContext(Dispatchers.IO) {
            try {
                Log.d("bbbb", "NextImage pocinja")
                if (response!!.isSuccessful) {
                    Log.d("bbbb", "NextImage isSuccessful")
                    for (exercise in response.body()?.results!!) {
                        exercise.let {
                            gymondoDatabase.exerciseDao().insertImage(exercise)
                            Log.d("bbbb", "ima nextImage pocinja" + response.body()!!.next!!)
                        }
                    }
                    if (!response.body()!!.next.isNullOrEmpty())
                        getNextImage(response.body()?.next!!)
                    Log.d("bbbb", "ima nextImage pocinja" + response.body()!!.next!!)
                } else {
                    Log.d("bbbb", "image ne e successful")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("bbbb", "next image" + e)
                withContext(Dispatchers.Main) {
                    exception.value = e
                }
            }
        }
        return response!!
    }

    suspend fun getNextExercise(next: String): Response<ExerciseResponse> {
        val response = gymondoApiService.getNextExercise(next)
        withContext(Dispatchers.IO) {
            val category = gymondoDatabase.exerciseDao().getAllCategoryList()
            val muscles = gymondoDatabase.exerciseDao().getMuscle()
            val equipment = gymondoDatabase.exerciseDao().getAllEquipmentList()
            val images = gymondoDatabase.exerciseDao().getImageList()
            val modelList = gymondoDatabase.exerciseDao().getAllNekojList()
            try {
                Log.d("bbbb", "pocna next exercise")
                if (response!!.isSuccessful) {
                    Log.d("bbbb", " next exercise isSuccessful")
                    for (exercise in response.body()!!.results!!) {
                        for (c in category) {
                            if (exercise.category == c.category_id) {
                                gymondoDatabase.exerciseDao()
                                    .insertNekoj(
                                        ModelExercise(
                                            exercise.id,
                                            exercise.description,
                                            exercise.name,
                                            c
                                        )
                                    )
                                Log.d("bbbb", " next exercise categorija zavrseno" )

                            }
                        }
                        for (muscle in muscles) {
                            val exList = gymondoDatabase.exerciseDao().getExerciseCategory()
                            val nekoj = gymondoDatabase.exerciseDao().getAllNekojList()
                            if (!exercise.muscles.isNullOrEmpty()) {
                                for (i in exercise.muscles) {
                                    if (i == muscle.id) {
                                        newMuscleList.add(muscle)
                                        if (newMuscleList.size == exercise.muscles.size) {
                                            gymondoDatabase.exerciseDao().updateTestOnly(
                                                newMuscleList, exercise.description!!
                                            )
                                            newMuscleList.clear()
                                            break
                                            Log.d("bbbb", " next exercise muskuli zavrseno" )

                                        }
                                    }
                                }
                            }
                        }
                        if (!exercise.equipment.isNullOrEmpty()) {
                            for (i in exercise.equipment) {
                                for (eq in equipment) {
                                    if (i == eq.id) {
                                        newEqList.add(eq)
                                        if (newEqList.size == exercise.equipment.size) {
                                            gymondoDatabase.exerciseDao().updateEquipment(
                                                newEqList, exercise.description!!
                                            )
                                            newEqList.clear()
                                            Log.d("bbbb", " next exercise oprema zavrseno" )

                                        }
                                    }

                                }
                            }
                        }
                        for (i in modelList) {
                            for (a in images) {
                                if (i.id == a.exercise) {
                                    newImageList.add(a)
                                    gymondoDatabase.exerciseDao()
                                        .updateImages(newImageList, i.description!!)
                                    newImageList.clear()
                                }
                                Log.d("bbbb", " next exercise sliki zavrseno" )

                            }
                        }
                        withContext(Dispatchers.Main) {
                            boolean.value = false
                            nextPage.value = response.body()?.next
                        }
                    }
                } else {
                    Log.d("bbbb", "exercise ne e successful")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("bbbb", "next exer" + e)
                withContext(Dispatchers.Main) {
                    exception.value = e
                }
            }
        }
        return response!!
    }
}