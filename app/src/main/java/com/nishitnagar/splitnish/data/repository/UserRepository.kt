package com.nishitnagar.splitnish.data.repository

import com.nishitnagar.splitnish.data.dao.UserDao
import com.nishitnagar.splitnish.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(
  private val userDao: UserDao,
) {
  suspend fun insert(userEntity: UserEntity) = userDao.insert(userEntity)

  suspend fun update(userEntity: UserEntity) = userDao.update(userEntity)

  fun getUserEntitiesFlow(): Flow<List<UserEntity>> = userDao.getUserEntitiesFlow()

  suspend fun getActiveUserEntity(): UserEntity? = userDao.getActiveUserEntity()
}