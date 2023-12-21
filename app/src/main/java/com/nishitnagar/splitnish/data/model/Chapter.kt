package com.nishitnagar.splitnish.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.data.entity.ChapterEntity

data class Chapter(
  @Embedded val chapterEntity: ChapterEntity,
  @Relation(
    parentColumn = "id",
    entityColumn = "chapter_id",
  )
  val categoryEntities: List<CategoryEntity>,
)
