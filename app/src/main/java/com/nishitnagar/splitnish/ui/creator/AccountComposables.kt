package com.nishitnagar.splitnish.ui.creator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.nishitnagar.splitnish.R
import com.nishitnagar.splitnish.data.entity.AccountEntity
import com.nishitnagar.splitnish.ui.composable.CustomTextField

class AccountComposables {
  companion object {
    @Composable
    fun Update(
      providedEntity: AccountEntity?,
      onCreate: (Any) -> Unit,
      onUpdate: (Any) -> Unit,
      onDelete: (Any) -> Unit,
      onDismiss: () -> Unit,
    ) {
      val accountEntityState = remember { mutableStateOf(AccountEntity(label = "")) }
      if (providedEntity != null) accountEntityState.value = providedEntity

      Column {
        Text(text = "Create ${stringResource(R.string.account_entity)}")
        LazyColumn {
          item { SelectLabelRow(accountEntityState) }
        }
        ButtonsRow(
          isEntityProvided = providedEntity != null,
          accountEntityState = accountEntityState,
          onCreate = onCreate,
          onUpdate = onUpdate,
          onDismiss = onDismiss,
          onDelete = onDelete,
        )
      }
    }

    // region Label Row

    @Composable
    private fun SelectLabelRow(
      accountEntityState: MutableState<AccountEntity>,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = "Label")
        CustomTextField(value = accountEntityState.value.label, label = "", onValueChange = {
          accountEntityState.value = accountEntityState.value.copy(label = it)
        })
      }
    }

    // endregion

    // region Buttons Row

    @Composable
    private fun ButtonsRow(
      isEntityProvided: Boolean,
      accountEntityState: MutableState<AccountEntity>,
      onCreate: (Any) -> Unit,
      onUpdate: (Any) -> Unit,
      onDelete: (Any) -> Unit,
      onDismiss: () -> Unit,
    ) {
      Row {
        Button(enabled = accountEntityState.value.label.isNotBlank(), onClick = {
          if (isEntityProvided) onUpdate(accountEntityState.value) else onCreate(accountEntityState.value)
          onDismiss()
        }) {
          val buttonLabel =
            stringResource(if (isEntityProvided) R.string.update else R.string.create)
          Text(text = buttonLabel)
        }
        Button(onClick = { onDismiss() }) {
          Text(text = stringResource(R.string.cancel))
        }
        if (isEntityProvided) {
          Button(onClick = {
            onDelete(accountEntityState.value)
            onDismiss()
          }) {
            Text(text = stringResource(R.string.delete))
          }
        }
      }
    }

    //endregion
  }
}