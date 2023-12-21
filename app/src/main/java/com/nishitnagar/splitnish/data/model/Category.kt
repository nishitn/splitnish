package com.nishitnagar.splitnish.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.data.entity.ChapterEntity
import com.nishitnagar.splitnish.data.entity.SubCategoryEntity

data class Category(
  @Embedded val categoryEntity: CategoryEntity,
  @Relation(
    parentColumn = "id",
    entityColumn = "parent_category_id",
  )
  val subCategoryEntities: List<SubCategoryEntity>,
  @Relation(
    parentColumn = "chapter_id",
    entityColumn = "id",
  )
  val parentChapter: List<ChapterEntity>,
)
