package com.nishitnagar.splitnish.ui.creator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.enums.VisibilityState

@Composable
fun SelectCategoryPopup(
    visibilityState: MutableState<VisibilityState>,
    categoryEntities: State<List<CategoryEntity>>
) {
    val selectedEntity = remember{ mutableStateOf<CategoryEntity?>(null) }
    val onDismiss = { visibilityState.value = VisibilityState.HIDDEN }

    SelectionDialog(
        title = {
            Text(text = "Select Category")
        },
        content = {
            SelectLazyColumn(selectedEntity, categoryEntities, onDismiss)
        },
        onDismiss = onDismiss
    )
}

@Composable
fun SelectionDialog(
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissButtonText: String = "Cancel",
) {
    AlertDialog(
        title = title,
        text = content,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(dismissButtonText) }
        },
        modifier = modifier
    )
}

@Composable
fun SelectLazyColumn(
    selectedEntity: MutableState<CategoryEntity?>,
    entities: State<List<CategoryEntity>>,
    onDismiss: () -> Unit,
) {
    LazyColumn {
        items(entities.value) { entity ->
            SelectEntityRow(
                categoryEntity = entity,
                selectedEntity = selectedEntity,
                onDismiss = onDismiss,
                secondaryVisibilityState = null
            )
        }
    }
}

@Composable
fun SelectEntityRow(
    categoryEntity: CategoryEntity,
    selectedEntity: MutableState<CategoryEntity?>,
    onDismiss: () -> Unit,
    secondaryVisibilityState: MutableState<VisibilityState>? = null,
) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(0.95f)
            .clickable {
                selectedEntity.value = categoryEntity
                onDismiss()
            }
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(text = categoryEntity.label, modifier = Modifier.fillMaxWidth())
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "More",
            modifier = Modifier.fillMaxHeight()
        )
    }
}

@Composable
fun ControlSelectPopup(
    visibilityState: MutableState<VisibilityState>, popup: @Composable () -> Unit
) {
    when (visibilityState.value) {
        VisibilityState.VISIBLE -> {
            popup()
        }
        VisibilityState.HIDDEN -> {
            /* Do Nothing */
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSelectionDialog() {
    SelectionDialog(
        title = { Text("Hi") },
        content = {
            Column {
              Box(modifier = Modifier.height(56.dp).fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                  Text("Item 1")
              }
              Box(modifier = Modifier.height(56.dp), contentAlignment = Alignment.CenterStart) {
                  Text("Item 2")
              }
              Box(modifier = Modifier.height(56.dp), contentAlignment = Alignment.CenterStart) {
                  Text("Item 3")
              }
              Box(modifier = Modifier.height(56.dp), contentAlignment = Alignment.CenterStart) {
                  Text("Item 4")
              }
            }
        },
        onDismiss = {},
    )
}

