package com.nishitnagar.splitnish.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nishitnagar.splitnish.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
  @Insert
  suspend fun insert(userEntity: UserEntity)

  @Update
  suspend fun update(userEntity: UserEntity)

  @Query("SELECT * FROM Users WHERE is_active LIMIT 1")
  suspend fun getActiveUserEntity(): UserEntity?

  @Query("SELECT * FROM Users")
  fun getUserEntitiesFlow(): Flow<List<UserEntity>>
}