package com.oliver.gymondo.database.daos

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oliver.gymondo.database.models.*

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise")
    fun getAllExercise(): LiveData<List<Exercise>>

    @Query("SELECT * FROM category")
    fun getAllCategoryList(): List<ExerciseCategory>

    @Query("SELECT * FROM nekojModel")
    fun getAllNekoj(): DataSource.Factory<Int, ModelExercise>

    @Query("SELECT * FROM nekojModel")
    fun getAllNekojList(): List<ModelExercise>

    @Query("SELECT * FROM equipment")
    fun getAllEquipmentList(): List<Equipment>

    @Query("SELECT * FROM muscle")
    fun getMuscle(): List<Muscle>

    @Query("SELECT * FROM image")
    fun getImageList(): List<ExerciseImage>

    @Query("SELECT * FROM exercise")
    fun getExerciseCategory(): List<Exercise>

    @Query("UPDATE nekojModel SET muscles = :price WHERE description = :id")
    fun updateTestOnly(price: List<Muscle>, id: String)

    @Query("SELECT * FROM nekojModel WHERE lower(name) = :name")
    fun searchByName(name: String): DataSource.Factory<Int, ModelExercise>

    @Query("SELECT * FROM nekojModel WHERE category_name = :name")
    fun searchByCategoryName(name: String): DataSource.Factory<Int, ModelExercise>

    @Query("SELECT * FROM nekojModel WHERE id = :id")
    fun searchById(id: Int): ModelExercise

    @Query("UPDATE nekojModel SET equipment = :price WHERE description = :id")
    fun updateEquipment(price: List<Equipment>, id: String)

    @Query("UPDATE nekojModel SET images = :price WHERE description = :id")
    fun updateImages(price: List<ExerciseImage>, id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNekoj(exercise: ModelExercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: ExerciseCategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEquipment(equipment: Equipment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMuscle(muscle: Muscle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(image: ExerciseImage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(exercises: List<Exercise>)
}