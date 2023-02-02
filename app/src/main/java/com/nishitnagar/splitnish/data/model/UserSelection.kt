package com.nishitnagar.splitnish.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.nishitnagar.splitnish.data.entity.AccountEntity
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.data.entity.ChapterEntity
import com.nishitnagar.splitnish.data.entity.UserSelectionEntity

data class UserSelection(
    @Embedded val userSelectionEntity: UserSelectionEntity,
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
