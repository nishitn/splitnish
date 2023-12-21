package com.nishitnagar.splitnish.data.dao

import androidx.room.*
import com.nishitnagar.splitnish.data.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
  @Insert
  suspend fun insert(accountEntity: AccountEntity)

  @Update
  suspend fun update(accountEntity: AccountEntity)

  @Delete
  suspend fun delete(accountEntity: AccountEntity)

  @Query("SELECT * FROM Accounts")
  suspend fun getAccountEntities(): List<AccountEntity>

  @Query("SELECT * FROM Accounts")
  fun getAccountEntitiesFlow(): Flow<List<AccountEntity>>
}