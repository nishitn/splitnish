package com.nishitnagar.splitnish.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Categories")
data class CategoryEntity(
    @ColumnInfo(name = "id") @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "label") val label: String
)