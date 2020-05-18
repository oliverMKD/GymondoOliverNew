package com.oliver.gymondo.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image")
class ExerciseImage (
    @PrimaryKey val id: Int,
    val image: String,
    val exercise : Int
)