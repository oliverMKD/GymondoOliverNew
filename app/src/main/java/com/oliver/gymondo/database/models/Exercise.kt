package com.oliver.gymondo.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise")
class Exercise(
    @PrimaryKey val id: Int,
    val description : String?,
    val name : String?,
    val category : Int?,
    val muscles : List<Int>?,
    val equipment : List<Int>?
)
//{
//    override fun toString() = name
//}

