package com.nishitnagar.splitnish.data.dao

import androidx.room.*
import com.nishitnagar.splitnish.data.entity.ChapterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {
    @Insert
    suspend fun insert(chapterEntity: ChapterEntity)

    @Update
    suspend fun update(chapterEntity: ChapterEntity)

    @Delete
    suspend fun delete(chapterEntity: ChapterEntity)

    @Query("SELECT * FROM Chapters")
    suspend fun getChapterEntities(): List<ChapterEntity>

    @Query("SELECT * FROM Chapters")
    fun getChapterEntitiesFlow(): Flow<List<ChapterEntity>>
}