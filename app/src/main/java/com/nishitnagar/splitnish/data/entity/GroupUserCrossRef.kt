package com.nishitnagar.splitnish.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.UUID

@Entity(primaryKeys = ["group_id", "user_id"], tableName = "Group_User_Cross_Ref")
data class GroupUserCrossRef(
  @ColumnInfo(name = "user_id") val userId: UUID,
  @ColumnInfo(name = "group_id") val groupId: UUID,
)
