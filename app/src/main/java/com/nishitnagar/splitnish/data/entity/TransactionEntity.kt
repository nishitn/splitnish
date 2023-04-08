package com.nishitnagar.splitnish.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nishitnagar.splitnish.enums.Currency
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "Transactions")
data class TransactionEntity(
    @ColumnInfo(name = "id") @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "date_time") val dateTime: LocalDateTime,
    @ColumnInfo(name = "currency") val currency: Currency,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "note") val note: String?,
)
