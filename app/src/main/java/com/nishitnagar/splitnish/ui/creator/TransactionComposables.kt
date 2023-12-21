@file:OptIn(ExperimentalMaterial3Api::class)

package com.nishitnagar.splitnish.ui.creator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.nishitnagar.splitnish.R
import com.nishitnagar.splitnish.data.entity.TransactionEntity
import com.nishitnagar.splitnish.data.model.Transaction
import com.nishitnagar.splitnish.enums.Currency
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.ui.composable.CustomNumberTextField
import com.nishitnagar.splitnish.ui.composable.CustomTextField
import com.nishitnagar.splitnish.util.DateTimeUtil
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

class TransactionComposables {
  companion object {
    @Composable
    fun Update(
      providedEntity: Transaction?,
      onCreate: (Any) -> Unit,
      onUpdate: (Any) -> Unit,
      onDelete: (Any) -> Unit,
      onDismiss: () -> Unit,
    ) {
      val transactionEntityState = remember {
        mutableStateOf(
          TransactionEntity(
            dateTime = LocalDateTime.now(),
            currency = Currency.INR,
            amount = null,
          )
        )
      }

      if (providedEntity != null) {
        transactionEntityState.value = providedEntity.transactionEntity
      }

      Column {
        Text(text = "Create ${stringResource(R.string.transaction_entity)}")
        LazyColumn {
          item { DateTimeRow(transactionEntityState) }
          item { AmountRow(transactionEntityState) }
          item { NoteRow(transactionEntityState) }
        }
        ButtonsRow(
          isEntityProvided = providedEntity != null,
          transactionEntityState = transactionEntityState,
          onCreate = onCreate,
          onUpdate = onUpdate,
          onDismiss = onDismiss,
          onDelete = onDelete,
        )
      }
    }

    // region DateTime Picker

    @Composable
    private fun DateTimeRow(
      transactionEntityState: MutableState<TransactionEntity>,
    ) {
      val datePickerPopupState = remember { mutableStateOf(VisibilityState.HIDDEN) }
      val timePickerPopupState = remember { mutableStateOf(VisibilityState.HIDDEN) }

      val initialDate = transactionEntityState.value.dateTime.toLocalDate()
      val initialTime = transactionEntityState.value.dateTime.toLocalTime()

      val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay(ZoneOffset.UTC).toInstant()
          .toEpochMilli()
      )
      val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour, initialMinute = initialTime.minute, is24Hour = true
      )

      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = "Date")
        Button(onClick = { datePickerPopupState.value = VisibilityState.VISIBLE }) {
          Text(text = DateTimeUtil.formatDate(transactionEntityState.value.dateTime.toLocalDate()))
        }
        Button(onClick = { timePickerPopupState.value = VisibilityState.VISIBLE }) {
          Text(text = DateTimeUtil.formatTime(transactionEntityState.value.dateTime.toLocalTime()))
        }
      }
      ControlDatePickerPopup(
        visibilityState = datePickerPopupState,
        datePickerState = datePickerState
      )
      ControlTimePickerPopup(
        visibilityState = timePickerPopupState,
        timePickerState = timePickerState
      )

      transactionEntityState.value = transactionEntityState.value.copy(
        dateTime = LocalDateTime.of(
          Instant.ofEpochMilli(datePickerState.selectedDateMillis!!).atZone(ZoneOffset.UTC)
            .toLocalDate(),
          LocalTime.of(timePickerState.hour, timePickerState.minute)
        )
      )
    }

    @Composable
    private fun ControlDatePickerPopup(
      visibilityState: MutableState<VisibilityState>, datePickerState: DatePickerState,
    ) {
      when (visibilityState.value) {
        VisibilityState.VISIBLE -> {
          SelectDatePicker(visibilityState, datePickerState)
        }

        VisibilityState.HIDDEN -> { /* Do Nothing */
        }
      }
    }

    @Composable
    private fun ControlTimePickerPopup(
      visibilityState: MutableState<VisibilityState>, timePickerState: TimePickerState,
    ) {
      when (visibilityState.value) {
        VisibilityState.VISIBLE -> {
          SelectTimePicker(visibilityState, timePickerState)
        }

        VisibilityState.HIDDEN -> { /* Do Nothing */
        }
      }
    }

    @Composable
    private fun SelectDatePicker(
      visibilityState: MutableState<VisibilityState>, datePickerState: DatePickerState,
    ) {
      DatePickerDialog(
        onDismissRequest = { visibilityState.value = VisibilityState.HIDDEN },
        confirmButton = {
          TextButton(onClick = {
            visibilityState.value = VisibilityState.HIDDEN
          }) { Text("OK") }
        },
        dismissButton = {
          TextButton(onClick = {
            visibilityState.value = VisibilityState.HIDDEN
          }) { Text("Cancel") }
        }) {
        DatePicker(state = datePickerState)
      }
    }

    @Composable
    private fun SelectTimePicker(
      visibilityState: MutableState<VisibilityState>, timePickerState: TimePickerState,
    ) {
      DatePickerDialog(
        onDismissRequest = { visibilityState.value = VisibilityState.HIDDEN },
        confirmButton = {
          TextButton(onClick = {
            visibilityState.value = VisibilityState.HIDDEN
          }) { Text("OK") }
        },
        dismissButton = {
          TextButton(onClick = {
            visibilityState.value = VisibilityState.HIDDEN
          }) { Text("Cancel") }
        }) {
        TimePicker(state = timePickerState)
      }
    }

    //endregion

    // region Amount Row

    @Composable
    private fun AmountRow(
      transactionEntityState: MutableState<TransactionEntity>,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = "Amount")
        CustomNumberTextField(
          value = transactionEntityState.value.amount,
          label = "",
          onValueChange = {
            transactionEntityState.value =
              transactionEntityState.value.copy(amount = it.toDoubleOrNull())
          })
      }
    }

    // endregion

    // region Note Row
    @Composable
    private fun NoteRow(
      transactionEntityState: MutableState<TransactionEntity>,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = "Note")
        CustomTextField(value = transactionEntityState.value.note, label = "", onValueChange = {
          transactionEntityState.value = transactionEntityState.value.copy(note = it)
        })
      }
    }

    // endregion

    // region Buttons Row

    @Composable
    private fun ButtonsRow(
      isEntityProvided: Boolean,
      transactionEntityState: MutableState<TransactionEntity>,
      onCreate: (Any) -> Unit,
      onUpdate: (Any) -> Unit,
      onDelete: (Any) -> Unit,
      onDismiss: () -> Unit,
    ) {
      Row {
        Button(enabled = transactionEntityState.value.amount != null, onClick = {
          if (isEntityProvided) onUpdate(transactionEntityState.value) else onCreate(
            transactionEntityState.value
          )
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
            onDelete(transactionEntityState.value)
            onDismiss()
          }) {
            Text(text = stringResource(R.string.delete))
          }
        }
      }
    }

    //endregion

    // region Parent Category Row

    //@Composable
    //fun SelectParentCategoryRow(
    //    selectedCategory: MutableState<CategoryEntity?>,
    //    visibilityState: MutableState<VisibilityState>,
    //    allCategories: State<List<Category>>,
    //) {
    //    val label = selectedCategory.value?.label
    //
    //    Row(
    //        verticalAlignment = Alignment.CenterVertically
    //    ) {
    //        Text(text = "Parent Category")
    //        CustomPopupOpenButton(label = label ?: "NULL", onClick = { visibilityState.value = VisibilityState.VISIBLE })
    //    }
    //}
    //
    //@Composable
    //fun SelectCategoryPopup(
    //    visibilityState: MutableState<VisibilityState>,
    //    allCategories: State<List<Category>>,
    //    selectedCategory: MutableState<CategoryEntity?>,
    //) {
    //    val onDismiss = { visibilityState.value = VisibilityState.HIDDEN }
    //    val parentCategoryId = remember { mutableStateOf(selectedCategory.value?.parentCategoryId) }
    //    val categoriesToShow = remember {
    //        mutableStateOf(allCategories.value.filter { it.categoryEntity.parentCategoryId == parentCategoryId.value }
    //            .map { it.categoryEntity }.toList<CategoryEntity?>())
    //    }
    //
    //    SelectionDialog(
    //        title = { Text(text = "Select Category") },
    //        content = {
    //            LazyColumn {
    //                item {
    //                    GoBackCategoryPopupRow(parentCategoryId, categoriesToShow, allCategories, onDismiss)
    //                }
    //                items(categoriesToShow.value) {
    //                    SelectCategoryPopupRow(
    //                        it, selectedCategory, categoriesToShow, parentCategoryId, allCategories, onDismiss
    //                    )
    //                }
    //            }
    //        },
    //        modifier = Modifier.padding(horizontal = 0.dp),
    //        onDismiss = onDismiss,
    //    )
    //}
    //
    //@Composable
    //fun SelectCategoryPopupRow(
    //    categoryEntity: CategoryEntity?,
    //    selectedCategory: MutableState<CategoryEntity?>,
    //    categoriesToShow: MutableState<List<CategoryEntity?>>,
    //    parentCategoryId: MutableState<UUID?>,
    //    allCategories: State<List<Category>>,
    //    onDismiss: () -> Unit
    //) {
    //    val category: Category?
    //    val categoryLabel: String
    //
    //    if (categoryEntity != null) {
    //        category = allCategories.value.firstOrNull { it.categoryEntity.id == categoryEntity.id }
    //        categoryLabel = Helper.getCategoryText(categoryEntity.id, allCategories)
    //    } else {
    //        category = null
    //        categoryLabel = "CATEGORY"
    //    }
    //
    //    Row(
    //        modifier = Modifier
    //            .height(56.dp)
    //            .fillMaxWidth(),
    //        verticalAlignment = Alignment.CenterVertically,
    //    ) {
    //        Box(contentAlignment = Alignment.Center, modifier = Modifier
    //            .fillMaxHeight()
    //            .weight(1f)
    //            .clickable {
    //                selectedCategory.value = categoryEntity
    //                onDismiss()
    //            }) {
    //            Text(text = categoryLabel, modifier = Modifier.fillMaxWidth())
    //        }
    //        if (category != null && category.subCategoryEntities.isNotEmpty()) {
    //            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
    //                .width(48.dp)
    //                .clickable {
    //                    parentCategoryId.value = category.categoryEntity.id
    //                    categoriesToShow.value = category.subCategoryEntities
    //                }) {
    //                Divider(
    //                    modifier = Modifier
    //                        .fillMaxHeight()
    //                        .width(1.dp)
    //                )
    //                Icon(
    //                    imageVector = Icons.Default.KeyboardArrowRight,
    //                    contentDescription = "Expand",
    //                    modifier = Modifier.fillMaxWidth()
    //                )
    //            }
    //        }
    //    }
    //}
    //
    //@Composable
    //fun GoBackCategoryPopupRow(
    //    parentCategoryId: MutableState<UUID?>,
    //    categoriesToShow: MutableState<List<CategoryEntity?>>,
    //    allCategories: State<List<Category>>,
    //    onDismiss: () -> Unit,
    //) {
    //    val label = if (parentCategoryId.value == null) "NONE" else ".."
    //
    //    Row(
    //        modifier = Modifier
    //            .height(56.dp)
    //            .fillMaxWidth(),
    //        verticalAlignment = Alignment.CenterVertically,
    //    ) {
    //        Box(contentAlignment = Alignment.Center, modifier = Modifier
    //            .fillMaxHeight()
    //            .weight(1f)
    //            .clickable {
    //                if (parentCategoryId.value != null) {
    //                    parentCategoryId.value = allCategories.value
    //                        .filter { it.categoryEntity.id == parentCategoryId.value }
    //                        .map { it.categoryEntity.parentCategoryId }
    //                        .firstOrNull()
    //
    //                    categoriesToShow.value = allCategories.value
    //                        .filter { it.categoryEntity.parentCategoryId == parentCategoryId.value }
    //                        .map { it.categoryEntity }
    //                } else {
    //                    onDismiss()
    //                }
    //            }) {
    //            Text(text = label, modifier = Modifier.fillMaxWidth(), fontStyle = FontStyle.Italic)
    //        }
    //    }
    //}

    // endregion
  }
}
