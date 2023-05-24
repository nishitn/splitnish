@file:OptIn(ExperimentalMaterial3Api::class)

package com.nishitnagar.splitnish.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nishitnagar.splitnish.util.Helper


@Composable
fun HeadingText(text: String) {
    Text(text = text, modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.headlineLarge)
}

@Composable
fun ConfirmationDialog(
    title: @Composable () -> Unit,
    text: @Composable () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmButtonText: String = "Confirm",
    dismissButtonText: String = "Cancel"
) {
    AlertDialog(title = title, text = text, onDismissRequest = onDismiss, confirmButton = {
        Button(onClick = onConfirm) {
            Text(confirmButtonText)
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text(dismissButtonText)
        }
    })
}

@Composable
fun CreateDialog(
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmButtonText: String = "Create",
    dismissButtonText: String = "Cancel"
) {
    AlertDialog(title = title, text = content, onDismissRequest = onDismiss, confirmButton = {
        Button(onClick = onConfirm) {
            Text(confirmButtonText)
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text(dismissButtonText)
        }
    })
}

@Composable
fun CustomTextField(
    value: String, label: String, modifier: Modifier = Modifier, onValueChange: (String) -> Unit
) {
    OutlinedTextField(value = value,
        modifier = modifier,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        onValueChange = onValueChange,
        singleLine = true
    )
}

@Composable
fun CustomNumberTextField(
    value: Double?, label: String, modifier: Modifier = Modifier, onValueChange: (String) -> Unit
) {
    val valueText = if (value != null) Helper.decimalFormat.format(value) else ""
    val state = remember { mutableStateOf(TextFieldValue(valueText)) }
    OutlinedTextField(value = state.value, modifier = modifier.onFocusChanged { focusState ->
        if (focusState.isFocused) {
            val text = state.value.text
            state.value = state.value.copy(selection = TextRange(0, text.length))
        }
    }, label = {
        if (label.isNotBlank()) {
            Text(text = label)
        }
    }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), trailingIcon = {
        if (state.value.text.toDoubleOrNull() == null) {
            Icon(Icons.Filled.Warning, "error", tint = MaterialTheme.colorScheme.error)
        }
    }, onValueChange = {
        state.value = it
        onValueChange(state.value.text)
    }, singleLine = true
    )
}

@Composable
fun CustomPopupOpenButton(
    label: String, modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Button(onClick = onClick, modifier = modifier) {
        Text(text = label)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomPopupOpenButton() {
    CustomPopupOpenButton(label = "Label", onClick = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewHeadingText() {
    HeadingText(text = "Heading")
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateDialog() {
    CreateDialog(
        title = {
            Text("Create Chapter Entity")
        },
        content = {
            CustomTextField(value = "Value", label = "Chapter Name", onValueChange = {})
        },
        onConfirm = {},
        onDismiss = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomTextField() {
    CustomTextField(value = "Value", label = "Label", onValueChange = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomNumberTextField() {
    CustomNumberTextField(value = 50.0, label = "Label", onValueChange = {})
}