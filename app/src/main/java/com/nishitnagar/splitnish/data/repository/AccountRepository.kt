package com.nishitnagar.splitnish.data.repository

import com.nishitnagar.splitnish.data.dao.AccountDao
import com.nishitnagar.splitnish.data.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

class AccountRepository(
    private val accountDao: AccountDao
) {
    suspend fun insert(accountEntity: AccountEntity) = accountDao.insert(accountEntity)

    suspend fun update(accountEntity: AccountEntity) = accountDao.update(accountEntity)

    suspend fun delete(accountEntity: AccountEntity) = accountDao.delete(accountEntity)

    suspend fun getAccountEntities(): List<AccountEntity> = accountDao.getAccountEntities()

    fun getAccountEntitiesFlow(): Flow<List<AccountEntity>> = accountDao.getAccountEntitiesFlow()
}