package com.nishitnagar.splitnish.ui.previewprovider

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.nishitnagar.splitnish.data.entity.*
import com.nishitnagar.splitnish.enums.Currency
import java.time.LocalDateTime

class ChapterEntitiesProvider : PreviewParameterProvider<State<List<ChapterEntity>>> {
  override val values = sequenceOf(
    mutableStateOf(
      listOf(
        ChapterEntity(label = "Indore"),
        ChapterEntity(label = "Honeywell"),
        ChapterEntity(label = "Bangalore"),
      )
    )
  )
}

class CategoryEntitiesProvider : PreviewParameterProvider<State<List<CategoryEntity>>> {
  override val values = sequenceOf(
    mutableStateOf(
      listOf(
        CategoryEntity(label = "Travel"),
        CategoryEntity(label = "Food"),
        CategoryEntity(label = "Social"),
      )
    )
  )
}

class AccountEntitiesProvider : PreviewParameterProvider<State<List<AccountEntity>>> {
  override val values = sequenceOf(
    mutableStateOf(
      listOf(
        AccountEntity(label = "Canara Bank"),
        AccountEntity(label = "PayTM"),
        AccountEntity(label = "Swiggy"),
      )
    )
  )
}

class UserEntitiesProvider : PreviewParameterProvider<State<List<UserEntity>>> {
  override val values = sequenceOf(
    mutableStateOf(
      listOf(
        UserEntity(firstName = "Nishit", lastName = "Nagar", username = "nishit", isActive = true),
        UserEntity(firstName = "Shashank", lastName = "Nagar", username = "shashank"),
        UserEntity(firstName = "Darshit", lastName = "Nagar", username = "darshit"),
      )
    )
  )
}

class TransactionEntitiesProvider : PreviewParameterProvider<State<List<TransactionEntity>>> {
  override val values = sequenceOf(
    mutableStateOf(
      listOf(
        TransactionEntity(
          dateTime = LocalDateTime.now(),
          currency = Currency.INR,
          amount = 500.0,
          note = ""
        ),
        TransactionEntity(
          dateTime = LocalDateTime.now(),
          currency = Currency.INR,
          amount = 1500.0,
          note = ""
        ),
        TransactionEntity(
          dateTime = LocalDateTime.now(),
          currency = Currency.INR,
          amount = 2500.0,
          note = ""
        ),
      )
    )
  )
}
