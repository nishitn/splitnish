package com.nishitnagar.splitnish.data.dao

import androidx.room.*
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    suspend fun insert(categoryEntity: CategoryEntity)

    @Update
    suspend fun update(categoryEntity: CategoryEntity)

    @Delete
    suspend fun delete(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM Categories")
    suspend fun getCategoryEntities(): List<CategoryEntity>

    @Query("SELECT * FROM Categories")
    fun getCategoryEntitiesFlow(): Flow<List<CategoryEntity>>
}