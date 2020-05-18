package com.oliver.gymondo.network.responses

import com.google.gson.annotations.SerializedName
import com.oliver.gymondo.database.models.Equipment
import com.oliver.gymondo.database.models.Muscle

class EquipmentResponse (
    val count: Int?,
    val next: String?,
    val previous: String?,
    @SerializedName("results") val results: ArrayList<Equipment>?
)