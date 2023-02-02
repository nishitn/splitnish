package com.nishitnagar.splitnish.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Group_Selections")
data class GroupSelectionEntity(
    @ColumnInfo(name = "id") @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "transaction_id") val transactionId: UUID,
    @ColumnInfo(name = "creator_id") val creatorId: UUID,
    @ColumnInfo(name = "chapter_id") val chapterId: UUID?,
    @ColumnInfo(name = "account_id") val accountId: UUID?,
    @ColumnInfo(name = "category_id") val categoryId: UUID?
)
