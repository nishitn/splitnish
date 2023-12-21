package com.nishitnagar.splitnish.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Users")
data class UserEntity(
  @ColumnInfo(name = "id") @PrimaryKey val id: UUID = UUID.randomUUID(),
  @ColumnInfo(name = "first_name") val firstName: String,
  @ColumnInfo(name = "last_man") val lastName: String?,
  @ColumnInfo(name = "username") val username: String,
  @ColumnInfo(name = "is_active") val isActive: Boolean = false,
)