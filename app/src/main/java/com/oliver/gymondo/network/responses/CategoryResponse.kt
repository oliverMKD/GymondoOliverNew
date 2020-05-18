package com.oliver.gymondo.network.responses

import com.google.gson.annotations.SerializedName
import com.oliver.gymondo.database.models.Exercise
import com.oliver.gymondo.database.models.ExerciseCategory

class CategoryResponse (
    val count: Int?,
    val next: String?,
    val previous: String?,
    @SerializedName("results") val results: List<ExerciseCategory>?
)