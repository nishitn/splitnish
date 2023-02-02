package com.nishitnagar.splitnish.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nishitnagar.splitnish.data.entity.TransactionEntity
import com.nishitnagar.splitnish.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction as RoomTransaction

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transactionEntity: TransactionEntity)

    @RoomTransaction
    @Query("SELECT * FROM Transactions")
    suspend fun getTransactionEntities(): List<TransactionEntity>

    @RoomTransaction
    @Query("SELECT * FROM Transactions")
    fun getTransactionEntitiesFlow(): Flow<List<TransactionEntity>>

    @RoomTransaction
    @Query("SELECT * FROM Transactions")
    suspend fun getTransactions(): List<Transaction>

    @RoomTransaction
    @Query("SELECT * FROM Transactions")
    fun getTransactionsFlow(): Flow<List<Transaction>>
}