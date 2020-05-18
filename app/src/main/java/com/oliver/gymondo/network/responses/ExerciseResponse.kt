package com.oliver.gymondo.network.responses

import com.google.gson.annotations.SerializedName
import com.oliver.gymondo.database.models.Exercise

class ExerciseResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    @SerializedName("results") val results: MutableList<Exercise>?
)