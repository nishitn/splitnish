package com.nishitnagar.splitnish.viewmodel

import androidx.lifecycle.ViewModel
import com.nishitnagar.splitnish.data.entity.GroupEntity
import com.nishitnagar.splitnish.data.entity.GroupUserCrossRef
import com.nishitnagar.splitnish.data.entity.UserEntity
import com.nishitnagar.splitnish.data.repository.GroupRepository
import com.nishitnagar.splitnish.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
): ViewModel() {
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    val userEntities: Flow<List<UserEntity>> = userRepository.getUserEntitiesFlow()

    fun getActiveUserEntity(): UserEntity? {
        val userEntity: UserEntity?
        runBlocking {
            userEntity = userRepository.getActiveUserEntity()
        }
        return userEntity
    }

    fun insert(userEntity: UserEntity) {
        ioScope.launch {
            userRepository.insert(userEntity)
        }
    }

    fun insert(groupEntity: GroupEntity, groupUserCrossRef: GroupUserCrossRef) {
        ioScope.launch {
            groupRepository.insert(groupEntity)
            groupRepository.insert(groupUserCrossRef)
        }
    }

    fun update(userEntity: UserEntity) {
        ioScope.launch {
            userRepository.update(userEntity)
        }
    }

    fun update(groupEntity: GroupEntity) {
        ioScope.launch {
            groupRepository.update(groupEntity)
        }
    }

    fun update(groupUserCrossRef: GroupUserCrossRef) {
        ioScope.launch {
            groupRepository.update(groupUserCrossRef)
        }
    }
}