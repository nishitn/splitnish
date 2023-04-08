package com.nishitnagar.splitnish.data.repository

import com.nishitnagar.splitnish.data.dao.TransactionDao
import com.nishitnagar.splitnish.data.entity.GroupSelectionEntity
import com.nishitnagar.splitnish.data.entity.TransactionEntity
import com.nishitnagar.splitnish.data.entity.UserSelectionEntity
import com.nishitnagar.splitnish.data.model.Transaction
import kotlinx.coroutines.flow.Flow

class TransactionRepository(
    private val transactionDao: TransactionDao
) {
    suspend fun insert(userSelectionEntity: UserSelectionEntity) = transactionDao.insert(userSelectionEntity)

    suspend fun insert(groupSelectionEntity: GroupSelectionEntity) = transactionDao.insert(groupSelectionEntity)

    suspend fun insert(transactionEntity: TransactionEntity) = transactionDao.insert(transactionEntity)

    suspend fun getTransactionEntities(): List<TransactionEntity> = transactionDao.getTransactionEntities()

    fun getTransactionEntitiesFlow(): Flow<List<TransactionEntity>> = transactionDao.getTransactionEntitiesFlow()

    suspend fun getTransactions(): List<Transaction> = transactionDao.getTransactions()

    fun getTransactionsFlow(): Flow<List<Transaction>> = transactionDao.getTransactionsFlow()
}