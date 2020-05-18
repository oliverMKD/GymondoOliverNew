package com.oliver.gymondo.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "category")
class ExerciseCategory(
    @PrimaryKey
    @SerializedName("id" )
    val category_id: Int?,
    @SerializedName("name" )
    val category_name: String?
)