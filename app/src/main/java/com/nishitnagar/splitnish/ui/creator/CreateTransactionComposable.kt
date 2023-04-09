@file:OptIn(ExperimentalMaterial3Api::class)

package com.nishitnagar.splitnish.ui.creator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.ui.composable.CustomNumberTextField
import com.nishitnagar.splitnish.ui.composable.CustomPopupOpenButton
import com.nishitnagar.splitnish.ui.composable.CustomTextField
import com.nishitnagar.splitnish.util.DateTimeUtil
import com.nishitnagar.splitnish.viewmodel.TransactionViewModel
import kotlinx.coroutines.flow.Flow
import java.time.*

@Composable
fun CreateTransactionComposable(transactionViewModel: TransactionViewModel) {
    val userChapterEntities = transactionViewModel.getUserChapterEntities()
    val userCategoryEntitiesFlow = transactionViewModel.categoryEntities
    Column {
        SelectDateTimeRow()
        SelectAmountRow()
        SelectNotesRow()
        SelectDescriptionRow()
        SelectUserCategoryRow(userCategoryEntitiesFlow)
    }
}

@Composable
fun SelectUserCategoryRow(
    userCategoryEntitiesFlow: Flow<List<CategoryEntity>>
) {
    val userCategoryEntities = userCategoryEntitiesFlow.collectAsState(initial = emptyList())
    val popupVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Chapter")
        CustomPopupOpenButton(label = "Chapter", onClick = { popupVisibilityState.value = VisibilityState.VISIBLE })
    }

    ControlSelectPopup(
        visibilityState = popupVisibilityState,
        popup = { SelectCategoryPopup(popupVisibilityState, userCategoryEntities) }
    )
}

@Composable
fun SelectDateTimeRow() {
    val datePickerPopupState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val timePickerPopupState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    )
    val timeNow = LocalTime.now()
    val timePickerState = rememberTimePickerState(
        initialHour = timeNow.hour, initialMinute = timeNow.minute, is24Hour = true
    )

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Date")
        Button(onClick = {
            datePickerPopupState.value = VisibilityState.VISIBLE
        }) {
            Text(
                text = DateTimeUtil.formatDate(
                    Instant.ofEpochMilli(datePickerState.selectedDateMillis!!).atZone(ZoneOffset.UTC).toLocalDate()
                )
            )
        }
        Button(onClick = {
            timePickerPopupState.value = VisibilityState.VISIBLE
        }) {
            Text(text = DateTimeUtil.formatTime(LocalTime.of(timePickerState.hour, timePickerState.minute)))
        }
    }
    ControlDatePickerPopup(visibilityState = datePickerPopupState, datePickerState = datePickerState)
    ControlTimePickerPopup(visibilityState = timePickerPopupState, timePickerState = timePickerState)
}

@Composable
fun SelectAmountRow() {
    var amount: Double? = null
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Amount")
        CustomNumberTextField(value = amount, label = "", onValueChange = { amount = it.toDoubleOrNull() })
    }
}

@Composable
fun SelectNotesRow() {
    var string = ""
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Notes")
        CustomTextField(value = string, label = "", onValueChange = { string = it })
    }
}

@Composable
fun SelectDescriptionRow() {
    var string = ""
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Description")
        CustomTextField(value = string, label = "", onValueChange = { string = it })
    }
}

@Composable
fun ControlDatePickerPopup(
    visibilityState: MutableState<VisibilityState>, datePickerState: DatePickerState
) {
    when (visibilityState.value) {
        VisibilityState.VISIBLE -> {
            SelectDatePicker(visibilityState, datePickerState)
        }
        VisibilityState.HIDDEN -> {
            /* Do Nothing */
        }
    }
}

@Composable
fun ControlTimePickerPopup(
    visibilityState: MutableState<VisibilityState>, timePickerState: TimePickerState
) {
    when (visibilityState.value) {
        VisibilityState.VISIBLE -> {
            SelectTimePicker(visibilityState, timePickerState)
        }
        VisibilityState.HIDDEN -> {
            /* Do Nothing */
        }
    }
}

@Composable
fun SelectDatePicker(
    visibilityState: MutableState<VisibilityState>, datePickerState: DatePickerState
) {
    DatePickerDialog(onDismissRequest = { visibilityState.value = VisibilityState.HIDDEN }, confirmButton = {
        TextButton(onClick = { visibilityState.value = VisibilityState.HIDDEN }) { Text("OK") }
    }, dismissButton = {
        TextButton(onClick = { visibilityState.value = VisibilityState.HIDDEN }) { Text("Cancel") }
    }) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun SelectTimePicker(
    visibilityState: MutableState<VisibilityState>, timePickerState: TimePickerState
) {
    DatePickerDialog(onDismissRequest = { visibilityState.value = VisibilityState.HIDDEN }, confirmButton = {
        TextButton(onClick = { visibilityState.value = VisibilityState.HIDDEN }) { Text("OK") }
    }, dismissButton = {
        TextButton(onClick = { visibilityState.value = VisibilityState.HIDDEN }) { Text("Cancel") }
    }) {
        TimePicker(state = timePickerState)
    }
}