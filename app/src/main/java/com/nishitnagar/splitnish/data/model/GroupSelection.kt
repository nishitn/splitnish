package com.nishitnagar.splitnish.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.nishitnagar.splitnish.data.entity.*

data class GroupSelection(
    @Embedded val groupSelectionEntity: GroupSelectionEntity,
    @Relation(
        parentColumn = "chapter_id",
        entityColumn = "id"
    )
    val chapterEntity: ChapterEntity,
    @Relation(
        parentColumn = "account_id",
        entityColumn = "id"
    )
    val accountEntity: AccountEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val categoryEntity: CategoryEntity
)