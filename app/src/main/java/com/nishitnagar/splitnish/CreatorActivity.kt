package com.nishitnagar.splitnish

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.nishitnagar.splitnish.data.entity.AccountEntity
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.data.entity.ChapterEntity
import com.nishitnagar.splitnish.data.entity.SubCategoryEntity
import com.nishitnagar.splitnish.data.entity.TransactionEntity
import com.nishitnagar.splitnish.data.exception.DataException
import com.nishitnagar.splitnish.enums.BundleKeys
import com.nishitnagar.splitnish.ui.creator.AccountComposables
import com.nishitnagar.splitnish.ui.creator.CategoryComposables
import com.nishitnagar.splitnish.ui.creator.ChapterComposables
import com.nishitnagar.splitnish.ui.creator.TransactionComposables
import com.nishitnagar.splitnish.ui.theme.SplitnishTheme
import com.nishitnagar.splitnish.util.Helper
import com.nishitnagar.splitnish.viewmodel.TransactionViewModel
import com.nishitnagar.splitnish.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable
import java.util.UUID

@AndroidEntryPoint
class CreatorActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val inputEntityResource = intent.extras?.getInt(BundleKeys.INT_RESOURCE.name)
    val providedEntityIdString = intent.extras?.getString(BundleKeys.ENTITY_ID.name)
    val entityData = intent.getSerializableExtra(BundleKeys.ENTITY_INPUT_DATA.name)
    val providedEntityId =
      if (providedEntityIdString != null) UUID.fromString(providedEntityIdString) else null

    val transactionViewModel: TransactionViewModel by viewModels()
    val userViewModel: UserViewModel by viewModels()
    Helper.activeUserEntity = userViewModel.getActiveUserEntity()

    setContent {
      SplitnishTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          OpenUpdateComposable(
            inputEntityResource = inputEntityResource,
            providedEntityId = providedEntityId,
            entityData = entityData,
            transactionViewModel = transactionViewModel
          )
        }
      }
    }
  }
}

@Composable
fun OpenUpdateComposable(
  inputEntityResource: Int?,
  providedEntityId: UUID?,
  entityData: Serializable?,
  transactionViewModel: TransactionViewModel,
) {
  val accountEntities = transactionViewModel.accountEntities.collectAsState(initial = listOf())
  val categoryEntities = transactionViewModel.categoryEntities.collectAsState(initial = listOf())
  val subCategoryEntities =
    transactionViewModel.subCategoryEntities.collectAsState(initial = listOf())
  val chapterEntities = transactionViewModel.chapterEntities.collectAsState(initial = listOf())

  val transactions = transactionViewModel.transactions.collectAsState(initial = listOf())

  val activity = LocalContext.current as Activity

  val onCreate: (Any) -> Unit = {
    when (it) {
      is AccountEntity -> transactionViewModel.insert(it)
      is CategoryEntity -> transactionViewModel.insert(it)
      is SubCategoryEntity -> transactionViewModel.insert(it)
      is ChapterEntity -> transactionViewModel.insert(it)
      is TransactionEntity -> transactionViewModel.insert(it)
    }
  }

  val onUpdate: (Any) -> Unit = {
    when (it) {
      is AccountEntity -> transactionViewModel.update(it)
      is CategoryEntity -> transactionViewModel.update(it)
      is SubCategoryEntity -> transactionViewModel.update(it)
      is ChapterEntity -> transactionViewModel.update(it)
      is TransactionEntity -> throw DataException("TODO")
    }
  }

  val onDelete: (Any) -> Unit = {
    when (it) {
      is AccountEntity -> transactionViewModel.delete(it)
      is CategoryEntity -> transactionViewModel.delete(it)
      is SubCategoryEntity -> transactionViewModel.delete(it)
      is ChapterEntity -> transactionViewModel.delete(it)
      is TransactionEntity -> throw DataException("TODO")
    }
  }

  when (inputEntityResource) {
    R.string.category_entity -> {
      val providedEntity =
        if (providedEntityId != null) categoryEntities.value.firstOrNull { it.id == providedEntityId } else null
      CategoryComposables.Update(
        providedEntity = providedEntity,
        entityData = entityData,
        chapterEntities = chapterEntities,
        subCategoryEntities = subCategoryEntities,
        onCreate = onCreate,
        onUpdate = onUpdate,
        onDelete = onDelete,
        onDismiss = { activity.finish() },
      )
    }

    R.string.chapter_entity -> {
      val providedEntity =
        if (providedEntityId != null) chapterEntities.value.firstOrNull { it.id == providedEntityId } else null
      ChapterComposables.Update(
        providedEntity = providedEntity,
        categoryEntities = categoryEntities,
        onCreate = onCreate,
        onUpdate = onUpdate,
        onDelete = onDelete,
        onDismiss = { activity.finish() },
      )
    }

    R.string.account_entity -> {
      val providedEntity =
        if (providedEntityId != null) accountEntities.value.firstOrNull { it.id == providedEntityId } else null
      AccountComposables.Update(
        providedEntity = providedEntity,
        onCreate = onCreate,
        onUpdate = onUpdate,
        onDelete = onDelete,
        onDismiss = { activity.finish() },
      )
    }

    R.string.transaction_entity -> {
      if (providedEntityId == null) {
        TransactionComposables.Update(
          providedEntity = null,
          onCreate = onCreate,
          onUpdate = onUpdate,
          onDelete = onDelete,
          onDismiss = { activity.finish() },
        )
      } else {
        val providedEntity =
          transactions.value.first { it.transactionEntity.id == providedEntityId }
        TransactionComposables.Update(
          providedEntity = providedEntity,
          onCreate = onCreate,
          onUpdate = onUpdate,
          onDelete = onDelete,
          onDismiss = { activity.finish() },
        )
      }
    }

    else -> Text(text = "To be Implemented")
  }
}