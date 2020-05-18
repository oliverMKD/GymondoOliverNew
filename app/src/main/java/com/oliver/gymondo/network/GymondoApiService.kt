package com.oliver.gymondo.network

import com.oliver.gymondo.network.responses.*
import retrofit2.Response
import retrofit2.http.*

interface GymondoApiService {
    @Headers("Connection:keep-alive")
    @GET("api/v2/exercise.json/")
    suspend fun getExercises(): Response<ExerciseResponse>?

    @GET("api/v2/equipment.json/")
    suspend fun getEquipment(): Response<EquipmentResponse>?

    @GET("api/v2/exercisecategory.json/")
    suspend fun getCategory(): Response<CategoryResponse>?

    @GET("api/v2/muscle.json")
    suspend fun getMuscle(): Response<MuscleResponse>?

    @GET("api/v2/exerciseimage.json")
    suspend fun getImage():Response<ExerciseImageResponse>?

    @GET
    suspend fun getNextImage(@Url next : String):Response<ExerciseImageResponse>?

    @GET
    suspend fun getNextExercise(@Url next : String):Response<ExerciseResponse>?

}