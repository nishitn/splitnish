package com.nishitnagar.splitnish.data.repository

import com.nishitnagar.splitnish.data.dao.GroupDao
import com.nishitnagar.splitnish.data.entity.GroupEntity
import com.nishitnagar.splitnish.data.entity.GroupUserCrossRef
import com.nishitnagar.splitnish.data.model.GroupWithUsers
import kotlinx.coroutines.flow.Flow

class GroupRepository(
    private val groupDao: GroupDao,
) {
    suspend fun insert(groupEntity: GroupEntity) = groupDao.insert(groupEntity)

    suspend fun update(groupEntity: GroupEntity) = groupDao.update(groupEntity)

    suspend fun delete(groupEntity: GroupEntity) = groupDao.delete(groupEntity)

    suspend fun insert(groupUserCrossRef: GroupUserCrossRef) = groupDao.insert(groupUserCrossRef)

    suspend fun update(groupUserCrossRef: GroupUserCrossRef) = groupDao.update(groupUserCrossRef)

    suspend fun delete(groupUserCrossRef: GroupUserCrossRef) = groupDao.delete(groupUserCrossRef)

    fun getGroupWithUsersFlow(): Flow<List<GroupWithUsers>> = groupDao.getGroupWithUsersFlow()
}