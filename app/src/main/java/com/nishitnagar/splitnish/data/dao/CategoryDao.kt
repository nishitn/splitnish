package com.nishitnagar.splitnish.data.dao

import androidx.room.*
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.data.entity.SubCategoryEntity
import com.nishitnagar.splitnish.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
  @Insert
  suspend fun insert(categoryEntity: CategoryEntity)

  @Update
  suspend fun update(categoryEntity: CategoryEntity)

  @Delete
  suspend fun delete(categoryEntity: CategoryEntity)

  @Insert
  suspend fun insert(subCategoryEntity: SubCategoryEntity)

  @Update
  suspend fun update(subCategoryEntity: SubCategoryEntity)

  @Delete
  suspend fun delete(subCategoryEntity: SubCategoryEntity)

  @Query("SELECT * FROM Categories")
  fun getCategoryEntitiesFlow(): Flow<List<CategoryEntity>>

  @Query("SELECT * FROM SubCategories")
  fun getSubCategoryEntitiesFlow(): Flow<List<SubCategoryEntity>>

  @Transaction
  @Query("SELECT * FROM Categories")
  fun getCategoriesFlow(): Flow<List<Category>>
}