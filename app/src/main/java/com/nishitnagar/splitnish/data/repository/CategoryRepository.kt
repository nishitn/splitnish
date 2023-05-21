package com.nishitnagar.splitnish.data.repository

import com.nishitnagar.splitnish.data.dao.CategoryDao
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.data.entity.SubCategoryEntity
import com.nishitnagar.splitnish.data.model.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
    private val categoryDao: CategoryDao
) {
    suspend fun insert(categoryEntity: CategoryEntity) = categoryDao.insert(categoryEntity)

    suspend fun update(categoryEntity: CategoryEntity) = categoryDao.update(categoryEntity)

    suspend fun delete(categoryEntity: CategoryEntity) = categoryDao.delete(categoryEntity)

    suspend fun insert(subCategoryEntity: SubCategoryEntity) = categoryDao.insert(subCategoryEntity)

    suspend fun update(subCategoryEntity: SubCategoryEntity) = categoryDao.update(subCategoryEntity)

    suspend fun delete(subCategoryEntity: SubCategoryEntity) = categoryDao.delete(subCategoryEntity)

    fun getCategoryEntitiesFlow(): Flow<List<CategoryEntity>> = categoryDao.getCategoryEntitiesFlow()

    fun getSubCategoryEntitiesFlow(): Flow<List<SubCategoryEntity>> = categoryDao.getSubCategoryEntitiesFlow()

    fun getCategoriesFlow(): Flow<List<Category>> = categoryDao.getCategoriesFlow()
}