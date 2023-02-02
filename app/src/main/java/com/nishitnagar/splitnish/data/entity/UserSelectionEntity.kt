package com.nishitnagar.splitnish.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "User_Selections")
data class UserSelectionEntity(
    @ColumnInfo(name = "id") @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "transaction_id") val transactionId: UUID,
    @ColumnInfo(name = "chapter_id") val chapterId: UUID?,
    @ColumnInfo(name = "account_id") val accountId: UUID?,
    @ColumnInfo(name = "category_id") val categoryId: UUID?,
)
