package com.nishitnagar.splitnish.viewmodel

import androidx.lifecycle.ViewModel
import com.nishitnagar.splitnish.data.entity.AccountEntity
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.data.entity.ChapterEntity
import com.nishitnagar.splitnish.data.entity.TransactionEntity
import com.nishitnagar.splitnish.data.repository.AccountRepository
import com.nishitnagar.splitnish.data.repository.CategoryRepository
import com.nishitnagar.splitnish.data.repository.ChapterRepository
import com.nishitnagar.splitnish.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val chapterRepository: ChapterRepository,
    private val transactionRepository: TransactionRepository,
) : ViewModel() {
    private val ioScope = CoroutineScope(Dispatchers.IO)

    val accountEntities = accountRepository.getAccountEntitiesFlow()

    val categoryEntities = categoryRepository.getCategoryEntitiesFlow()

    val chapterEntities = chapterRepository.getChapterEntitiesFlow()

    val transactionEntities = transactionRepository.getTransactionEntitiesFlow()

    val transactions = transactionRepository.getTransactionsFlow()

    fun insert(accountEntity: AccountEntity) {
        ioScope.launch { accountRepository.insert(accountEntity) }
    }

    fun insert(categoryEntity: CategoryEntity) {
        ioScope.launch { categoryRepository.insert(categoryEntity) }
    }

    fun insert(chapterEntity: ChapterEntity) {
        ioScope.launch { chapterRepository.insert(chapterEntity) }
    }

    fun insert(transactionEntity: TransactionEntity) {
        ioScope.launch { transactionRepository.insert(transactionEntity) }
    }

    fun update(accountEntity: AccountEntity) {
        ioScope.launch { accountRepository.update(accountEntity) }
    }

    fun update(categoryEntity: CategoryEntity) {
        ioScope.launch { categoryRepository.update(categoryEntity) }
    }

    fun update(chapterEntity: ChapterEntity) {
        ioScope.launch { chapterRepository.update(chapterEntity) }
    }

    fun delete(accountEntity: AccountEntity) {
        ioScope.launch { accountRepository.delete(accountEntity) }
    }

    fun delete(categoryEntity: CategoryEntity) {
        ioScope.launch { categoryRepository.delete(categoryEntity) }
    }

    fun delete(chapterEntity: ChapterEntity) {
        ioScope.launch { chapterRepository.delete(chapterEntity) }
    }
}