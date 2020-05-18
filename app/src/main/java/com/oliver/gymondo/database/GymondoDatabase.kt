package com.oliver.gymondo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.oliver.gymondo.database.daos.ExerciseDao
import com.oliver.gymondo.database.models.*
import com.oliver.gymondo.exercise.utils.Converters

/**
 * The Room database for this app
 */
@Database(
    entities = [Exercise::class, ExerciseCategory::class, Equipment::class,
        Muscle::class, ExerciseImage::class, ModelExercise::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GymondoDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao

    companion object {

        private const val DB_NAME = "gymondo"

        // For Singleton instantiation
        @Volatile
        private var instance: GymondoDatabase? = null

        fun getInstance(context: Context): GymondoDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): GymondoDatabase {
            return Room.databaseBuilder(
                context, GymondoDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}