package com.nishitnagar.splitnish.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.nishitnagar.splitnish.data.entity.*

data class Transaction(
  @Embedded val transactionEntity: TransactionEntity,
  @Relation(
    entity = UserSelectionEntity::class,
    parentColumn = "id",
    entityColumn = "transaction_id"
  )
  val userSelection: UserSelection,
  @Relation(
    entity = GroupSelectionEntity::class,
    parentColumn = "id",
    entityColumn = "transaction_id"
  )
  val groupSelection: GroupSelection?,
)