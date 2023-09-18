package com.nishitnagar.splitnish.ui.creator

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nishitnagar.splitnish.CreatorActivity
import com.nishitnagar.splitnish.R
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.data.entity.ChapterEntity
import com.nishitnagar.splitnish.enums.BundleKeys
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.ui.composable.ConfirmationDialog
import com.nishitnagar.splitnish.ui.composable.CustomTextField

@Composable
fun UpdateChapterComposable(
    providedEntity: ChapterEntity,
    categoryEntities: State<List<CategoryEntity>>,
    onUpdate: (Any) -> Unit,
    onDelete: (Any) -> Unit,
    onDismiss: () -> Unit
) {
    val chapterEntityState = remember { mutableStateOf(providedEntity) }

    val selectedCategoryEntities = remember {
        derivedStateOf {
            categoryEntities.value.filter { it.chapterId == chapterEntityState.value.id }
        }
    }

    val context = LocalContext.current
    val onCreateNewCategory = {
        val entityData = HashMap<String, String>()
        entityData[BundleKeys.CHAPTER_ID.name] = chapterEntityState.value.id.toString()

        val intent = Intent(context, CreatorActivity::class.java)
        intent.putExtra(BundleKeys.INT_RESOURCE.name, R.string.category_entity)
        intent.putExtra(BundleKeys.ENTITY_INPUT_DATA.name, entityData)
        context.startActivity(intent)
    }

    Column {
        Text(text = "${stringResource(R.string.update)} ${stringResource(R.string.chapter_entity)}")
        LazyColumn {
            item { SelectChapterLabelRow(chapterEntityState) }
            item { SelectCategoryEntitiesRow(selectedCategoryEntities, onCreateNewCategory, onDelete) }
        }
        ChapterUpdateButtonsRow(
            chapterEntityState = chapterEntityState,
            onUpdate = onUpdate,
            onDelete = onDelete,
            onDismiss = onDismiss,
        )
    }
}

// region Label Row

@Composable
fun SelectChapterLabelRow(
    entityState: MutableState<ChapterEntity>,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Label")
        CustomTextField(value = entityState.value.label, label = "", onValueChange = {
            entityState.value = entityState.value.copy(label = it)
        })
    }
}

// endregion

// region Category Row

@Composable
fun SelectCategoryEntitiesRow(
    selectedCategoryEntities: State<List<CategoryEntity>>,
    onAdd: () -> Unit,
    onDelete: (Any) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Categories")
        CategoryEntitiesColumn(selectedCategoryEntities, onAdd, onDelete)
    }
}

@Composable
fun CategoryEntitiesColumn(
    selectedCategoryEntities: State<List<CategoryEntity>>,
    onAdd: () -> Unit,
    onDelete: (Any) -> Unit,
) {
    Column {
        selectedCategoryEntities.value.forEach {
            CategoryEntityRow(categoryEntity = it, onDelete)
        }
        AddMoreCategoryRow(onAdd)
    }
}

@Composable
fun CategoryEntityRow(
    categoryEntity: CategoryEntity,
    onDelete: (Any) -> Unit,
) {
    val removeCategoryDialogState = remember { mutableStateOf(VisibilityState.HIDDEN) }

    Column {
        Row {
            Text(text = categoryEntity.label)
            IconButton(onClick = { removeCategoryDialogState.value = VisibilityState.VISIBLE }) {
                Icon(Icons.Default.Edit, "Edit Category")
            }
        }
        HorizontalDivider(thickness = 1.dp)
    }

    ControlConfirmDeleteDialog(removeCategoryDialogState, categoryEntity, onDelete)
}

@Composable
fun AddMoreCategoryRow(
    onAdd: () -> Unit,
) {
    Row {
        Button(onClick = onAdd) {
            Text(text = "Add More")
        }
    }
}

@Composable
fun ControlConfirmDeleteDialog(
    visibilityState: MutableState<VisibilityState>,
    categoryEntity: CategoryEntity,
    onDelete: (Any) -> Unit,
) {
    when (visibilityState.value) {
        VisibilityState.VISIBLE -> {
            ConfirmationDialog(title = { Text("Confirm Remove Category") },
                text = { Text(text = "Are you sure you want to remove Category: ${categoryEntity.label}?") },
                onConfirm = {
                    onDelete(categoryEntity)
                    visibilityState.value = VisibilityState.HIDDEN
                },
                onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
        }

        VisibilityState.HIDDEN -> {
            /* Do Nothing */
        }
    }
}

// endregion

// region Update Button

@Composable
fun ChapterUpdateButtonsRow(
    chapterEntityState: MutableState<ChapterEntity>,
    onUpdate: (Any) -> Unit,
    onDelete: (Any) -> Unit,
    onDismiss: () -> Unit,
) {
    Row {
        Button(
            enabled = chapterEntityState.value.label.isNotBlank(),
            onClick = {
                onUpdate(chapterEntityState.value)
                onDismiss()
            }
        ) {
            val buttonLabel = stringResource(R.string.update)
            Text(text = buttonLabel)
        }
        Button(onClick = { onDismiss() }) {
            Text(text = stringResource(R.string.cancel))
        }
        Button(onClick = {
            onDelete(chapterEntityState.value)
            onDismiss()
        }) {
            Text(text = stringResource(R.string.delete))
        }
    }
}

//endregion
