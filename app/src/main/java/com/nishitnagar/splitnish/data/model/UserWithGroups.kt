package com.nishitnagar.splitnish.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.nishitnagar.splitnish.data.entity.GroupEntity
import com.nishitnagar.splitnish.data.entity.GroupUserCrossRef
import com.nishitnagar.splitnish.data.entity.UserEntity

data class UserWithGroups(
    @Embedded val userEntity: UserEntity,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "group_id",
        associateBy = Junction(GroupUserCrossRef::class)
    )
    val groupEntities: List<GroupEntity>
)