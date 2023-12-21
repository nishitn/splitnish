package com.nishitnagar.splitnish.data.repository

import com.nishitnagar.splitnish.data.dao.ChapterDao
import com.nishitnagar.splitnish.data.entity.ChapterEntity
import com.nishitnagar.splitnish.data.model.Chapter
import kotlinx.coroutines.flow.Flow

class ChapterRepository(
  private val chapterDao: ChapterDao,
) {
  suspend fun insert(chapterEntity: ChapterEntity) = chapterDao.insert(chapterEntity)

  suspend fun update(chapterEntity: ChapterEntity) = chapterDao.update(chapterEntity)

  suspend fun delete(chapterEntity: ChapterEntity) = chapterDao.delete(chapterEntity)

  fun getChapterEntitiesFlow(): Flow<List<ChapterEntity>> = chapterDao.getChapterEntitiesFlow()

  fun getChaptersFlow(): Flow<List<Chapter>> = chapterDao.getChaptersFlow()
}