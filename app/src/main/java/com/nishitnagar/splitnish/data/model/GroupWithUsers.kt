package com.nishitnagar.splitnish.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.nishitnagar.splitnish.data.entity.GroupEntity
import com.nishitnagar.splitnish.data.entity.GroupUserCrossRef
import com.nishitnagar.splitnish.data.entity.UserEntity

data class GroupWithUsers(
    @Embedded val groupEntity: GroupEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = GroupUserCrossRef::class,
            parentColumn = "group_id",
            entityColumn = "user_id"
        )
    )
    val userEntities: List<UserEntity>
)
