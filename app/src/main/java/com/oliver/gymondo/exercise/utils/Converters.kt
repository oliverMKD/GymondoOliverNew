package com.oliver.gymondo.exercise.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oliver.gymondo.database.models.Equipment
import com.oliver.gymondo.database.models.ExerciseImage
import com.oliver.gymondo.database.models.Muscle

class Converters {

    private val gson = Gson()

    private fun <T> fromJson(data: String?, classType: Class<T>): List<T>? {
        if (data == null) {
            return null
        }
        val type = TypeToken.getParameterized(MutableList::class.java, classType).type
        return gson.fromJson(data, type)
    }

    private fun <T> toJson(someObject: List<T>): String? {
        return gson.toJson(someObject)
    }

    @TypeConverter
    fun toListMuscleItems(jsonString: String?): List<Muscle?>? {
        return fromJson(jsonString, Muscle::class.java)
    }

    @TypeConverter
    fun fromListMuscleItems(muscleItems: List<Muscle>): String? {
        return toJson(muscleItems)
    }

    @TypeConverter
    fun listToJson(value: List<Int>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Int>::class.java).toList()

    @TypeConverter
    fun listToJsonFromString(value: List<Equipment>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToListString(value: String) =
        Gson().fromJson(value, Array<Equipment>::class.java).toMutableList()

    @TypeConverter
    fun listToJsonFromStringImages(value: List<ExerciseImage>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToListStringImages(value: String) =
        Gson().fromJson(value, Array<ExerciseImage>::class.java).toList()

}