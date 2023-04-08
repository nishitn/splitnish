package com.nishitnagar.splitnish.data.dao

import androidx.room.*
import com.nishitnagar.splitnish.data.entity.GroupEntity
import com.nishitnagar.splitnish.data.entity.GroupUserCrossRef
import com.nishitnagar.splitnish.data.model.GroupWithUsers
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Insert
    suspend fun insert(groupEntity: GroupEntity)

    @Update
    suspend fun update(groupEntity: GroupEntity)

    @Delete
    suspend fun delete(groupEntity: GroupEntity)

    @Insert
    suspend fun insert(groupUserCrossRef: GroupUserCrossRef)

    @Update
    suspend fun update(groupUserCrossRef: GroupUserCrossRef)

    @Delete
    suspend fun delete(groupUserCrossRef: GroupUserCrossRef)

    @Transaction
    @Query("SELECT * FROM Groups")
    fun getGroupWithUsersFlow(): Flow<List<GroupWithUsers>>
}