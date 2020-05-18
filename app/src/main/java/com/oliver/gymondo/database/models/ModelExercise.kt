package com.oliver.gymondo.database.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nekojModel")
class ModelExercise(
    @PrimaryKey
    val id: Int?,
    val description : String?,
    val name : String?,
    @Embedded val category: ExerciseCategory?,
    val muscles: List<Muscle>? = arrayListOf(),
    val equipment : List<Equipment>? = arrayListOf(),
    val images : List<ExerciseImage>? = arrayListOf()

    )