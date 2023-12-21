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
import com.nishitnagar.splitnish.data.entity.UserEntity
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.ui.previewprovider.UserEntitiesProvider
import com.nishitnagar.splitnish.util.Helper


@Composable
fun ControlUserScreen(
    visibilityState: MutableState<VisibilityState>,
    userEntities: State<List<UserEntity>>,
    onCreate: (UserEntity) -> Unit,
) {
  when (visibilityState.value) {
    VisibilityState.VISIBLE -> {
      UserScreen(userEntities = userEntities,
                 onCreate = onCreate,
                 onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
    }

    VisibilityState.HIDDEN -> {
      /* Do Nothing */
    }
  }
}

@Composable
fun UserScreen(
    userEntities: State<List<UserEntity>>, onCreate: (UserEntity) -> Unit, onDismiss: () -> Unit,
) {
  val dialogVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
  Column {
    Row {
      Button(onClick = { dialogVisibilityState.value = VisibilityState.VISIBLE }) {
        Text("Create User")
      }
      Button(onClick = onDismiss) {
        Text("Dismiss")
      }
    }
    UserLazyColumn(userEntities = userEntities)
  }

  ControlCreateUserEntityDialog(dialogVisibilityState, onCreate = onCreate)
}

@Composable
fun UserLazyColumn(
    @PreviewParameter(UserEntitiesProvider::class) userEntities: State<List<UserEntity>>,
) {
  LazyColumn {
    item {
      HeadingText(text = "User Entities")
    }
    items(userEntities.value) { userEntity ->
      UserRow(userEntity)
    }
  }
}

@Composable
fun UserRow(item: UserEntity) {
  Text(text = Helper.gson.toJson(item), modifier = Modifier.padding(8.dp))
}

@Composable
fun ControlCreateUserEntityDialog(
    visibilityState: MutableState<VisibilityState>, onCreate: (UserEntity) -> Unit,
) {
  when (visibilityState.value) {
    VisibilityState.VISIBLE -> {
      CreateUserDialog(onCreate = {
        onCreate(it)
        visibilityState.value = VisibilityState.HIDDEN
      }, onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
    }

    VisibilityState.HIDDEN -> {
      if (Helper.activeUserEntity == null) visibilityState.value = VisibilityState.VISIBLE
    }
  }
}

@Composable
fun CreateUserDialog(onCreate: (UserEntity) -> Unit, onDismiss: () -> Unit) {
  var firstName = ""
  var lastName = ""
  var username = ""

  CreateDialog(title = { Text("Create User Entity") }, content = {
    Column {
      CustomTextField(value = firstName, label = "First Name", onValueChange = { firstName = it })
      CustomTextField(value = lastName, label = "Last Name", onValueChange = { lastName = it })
      CustomTextField(value = username, label = "Username", onValueChange = { username = it })
    }
  }, onConfirm = {
    onCreate(
      UserEntity(firstName = firstName, lastName = lastName, username = username)
    )
  }, onDismiss = onDismiss)
}

@Preview(showBackground = true)
@Composable
fun PreviewUserScreen() {
  val userEntities = remember {
    mutableStateOf(
      listOf(
        UserEntity(firstName = "Nishit", lastName = "Nagar", username = "nishit", isActive = true),
        UserEntity(firstName = "Shashank", lastName = "Nagar", username = "shashank"),
        UserEntity(firstName = "Darshit", lastName = "Nagar", username = "darshit"),
      )
    )
  }
  UserScreen(userEntities = userEntities, onCreate = {}, onDismiss = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateUserDialog() {
  CreateUserDialog(onCreate = {}, onDismiss = {})
}
