package com.nishitnagar.splitnish.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "SubCategories")
data class SubCategoryEntity(
  @ColumnInfo(name = "id") @PrimaryKey val id: UUID = UUID.randomUUID(),
  @ColumnInfo(name = "label") val label: String,
  @ColumnInfo(name = "parent_category_id") var parentCategoryId: UUID? = null,
)
