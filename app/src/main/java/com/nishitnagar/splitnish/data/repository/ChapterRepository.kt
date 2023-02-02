package com.nishitnagar.splitnish.data.repository

import com.nishitnagar.splitnish.data.dao.ChapterDao
import com.nishitnagar.splitnish.data.entity.ChapterEntity
import kotlinx.coroutines.flow.Flow

class ChapterRepository(
    private val chapterDao: ChapterDao
) {
    suspend fun insert(chapterEntity: ChapterEntity) = chapterDao.insert(chapterEntity)

    suspend fun update(chapterEntity: ChapterEntity) = chapterDao.update(chapterEntity)

    suspend fun delete(chapterEntity: ChapterEntity) = chapterDao.delete(chapterEntity)

    suspend fun getChapterEntities(): List<ChapterEntity> = chapterDao.getChapterEntities()

    fun getChapterEntitiesFlow(): Flow<List<ChapterEntity>> = chapterDao.getChapterEntitiesFlow()
}