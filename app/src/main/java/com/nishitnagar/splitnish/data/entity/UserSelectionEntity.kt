package com.nishitnagar.splitnish.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "User_Selections")
data class UserSelectionEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "transaction_id") val transactionId: UUID,
    @ColumnInfo(name = "chapter_id") val chapterId: UUID?,
    @ColumnInfo(name = "account_id") val accountId: UUID?,
    @ColumnInfo(name = "category_id") val categoryId: UUID?,
)
