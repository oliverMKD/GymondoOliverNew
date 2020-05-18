package com.oliver.gymondo.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "muscle")
class Muscle (
    @PrimaryKey val id: Int,
    val name: String,
    val is_front : Boolean
)