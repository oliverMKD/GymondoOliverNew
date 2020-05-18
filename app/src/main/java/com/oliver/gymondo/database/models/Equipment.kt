package com.oliver.gymondo.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipment")
class Equipment (
    @PrimaryKey val id: Int,
    val name: String?
)