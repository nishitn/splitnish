package com.nishitnagar.splitnish.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.nishitnagar.splitnish.data.entity.AccountEntity
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.ui.previewprovider.AccountEntitiesProvider
import com.nishitnagar.splitnish.util.Helper

@Composable
fun ControlAccountScreen(
    visibilityState: MutableState<VisibilityState>,
    accountEntities: State<List<AccountEntity>>,
    onCreate: (AccountEntity) -> Unit,
) {
  when (visibilityState.value) {
    VisibilityState.VISIBLE -> {
      AccountScreen(accountEntities = accountEntities,
                    onCreate = onCreate,
                    onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
    }

    VisibilityState.HIDDEN -> {
      /* Do Nothing */
    }
  }
}

@Composable
fun AccountScreen(
    accountEntities: State<List<AccountEntity>>,
    onCreate: (AccountEntity) -> Unit,
    onDismiss: () -> Unit,
) {
  val dialogVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
  Column {
    Row {
      Button(onClick = { dialogVisibilityState.value = VisibilityState.VISIBLE }) {
        Text("Create Account")
      }
      Button(onClick = onDismiss) {
        Text("Dismiss")
      }
    }
    AccountLazyColumn(accountEntities = accountEntities)
  }

  ControlCreateAccountEntityDialog(dialogVisibilityState, onCreate = onCreate)
}

@Composable
fun AccountLazyColumn(
    @PreviewParameter(AccountEntitiesProvider::class) accountEntities: State<List<AccountEntity>>,
) {
  LazyColumn {
    item {
      HeadingText(text = "Account Entities")
    }
    items(accountEntities.value) { accountEntity ->
      AccountRow(accountEntity)
    }
  }
}

@Composable
fun AccountRow(item: AccountEntity) {
  Text(text = Helper.gson.toJson(item), modifier = Modifier.padding(8.dp))
}

@Composable
fun ControlCreateAccountEntityDialog(
    visibilityState: MutableState<VisibilityState>, onCreate: (AccountEntity) -> Unit,
) {
  when (visibilityState.value) {
    VisibilityState.VISIBLE -> {
      CreateAccountDialog(onCreate = {
        onCreate(it)
        visibilityState.value = VisibilityState.HIDDEN
      }, onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
    }

    VisibilityState.HIDDEN -> {
      /* Do Nothing */
    }
  }
}

@Composable
fun CreateAccountDialog(onCreate: (AccountEntity) -> Unit, onDismiss: () -> Unit) {
  var label = ""
  CreateDialog(title = { Text("Create Account Entity") }, content = {
    CustomTextField(value = label, label = "Label", onValueChange = { label = it })
  }, onConfirm = {
    onCreate(
      AccountEntity(label = label)
    )
  }, onDismiss = onDismiss)
}

@Preview(showBackground = true)
@Composable
fun PreviewAccountScreen() {
  val accountEntities = remember {
    mutableStateOf(
      listOf(
        AccountEntity(label = "Indore"),
        AccountEntity(label = "Honeywell"),
        AccountEntity(label = "Bangalore"),
      )
    )
  }
  AccountScreen(accountEntities = accountEntities, onCreate = {}, onDismiss = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateAccountDialog() {
  CreateAccountDialog(onCreate = {}, onDismiss = {})
}
