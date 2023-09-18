package com.nishitnagar.splitnish.ui.creator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nishitnagar.splitnish.R
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.data.entity.ChapterEntity
import com.nishitnagar.splitnish.data.entity.SubCategoryEntity
import com.nishitnagar.splitnish.enums.BundleKeys
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.ui.composable.ConfirmationDialog
import com.nishitnagar.splitnish.ui.composable.CreateDialog
import com.nishitnagar.splitnish.ui.composable.CustomPopupOpenButton
import com.nishitnagar.splitnish.ui.composable.CustomTextField
import com.nishitnagar.splitnish.util.Helper
import java.io.Serializable
import java.util.UUID

@Composable
fun UpdateCategoryComposable(
    providedEntity: CategoryEntity?,
    entityData: Serializable?,
    chapterEntities: State<List<ChapterEntity>>,
    subCategoryEntities: State<List<SubCategoryEntity>>,
    onCreate: (Any) -> Unit,
    onUpdate: (Any) -> Unit,
    onDelete: (Any) -> Unit,
    onDismiss: () -> Unit,
) {
    val selectedChapterId = UUID.fromString(Helper.getKeyFromSerializedMap(entityData, BundleKeys.CHAPTER_ID.name))
    val categoryEntityState = remember { mutableStateOf(CategoryEntity(label = "", chapterId = selectedChapterId)) }
    if (providedEntity != null) {
        categoryEntityState.value = providedEntity
    }

    val selectedChapter = remember {
        derivedStateOf {
            if (categoryEntityState.value.chapterId != null) {
                chapterEntities.value.first { it.id == categoryEntityState.value.chapterId }
            } else {
                null
            }
        }
    }
    val selectedSubCategoryEntities = remember {
        derivedStateOf {
            subCategoryEntities.value.filter { it.parentCategoryId == categoryEntityState.value.id }
        }
    }
    val newSubCategoryEntities = remember { mutableStateListOf<SubCategoryEntity>() }

    val selectChapterPopupVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }

    Column {
        Text(text = "Create ${stringResource(R.string.category_entity)}")
        LazyColumn {
            item { SelectCategoryLabelRow(categoryEntityState) }
            item { SelectCategoryParentChapterRow(selectedChapter, selectChapterPopupVisibilityState) }
            item { SelectSubCategoryEntitiesRow(selectedSubCategoryEntities, newSubCategoryEntities, onDelete) }
        }
        CategoryUpdateButtonsRow(
            isEntityProvided = providedEntity != null,
            categoryEntityState = categoryEntityState,
            newSubCategoryEntities = newSubCategoryEntities,
            onCreate = onCreate,
            onUpdate = onUpdate,
            onDismiss = onDismiss,
            onDelete = onDelete,
        )
    }

    ControlSelectPopup(visibilityState = selectChapterPopupVisibilityState) {
        SelectChapterPopup(
            visibilityState = selectChapterPopupVisibilityState,
            chapterEntities = chapterEntities,
            categoryEntityState = categoryEntityState,
        )
    }
}

// region Label Row

@Composable
fun SelectCategoryLabelRow(
    categoryEntityState: MutableState<CategoryEntity>,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Label")
        CustomTextField(value = categoryEntityState.value.label, label = "", onValueChange = {
            categoryEntityState.value = categoryEntityState.value.copy(label = it)
        })
    }
}

// endregion

// region Chapter Row

@Composable
fun SelectCategoryParentChapterRow(
    selectedChapter: State<ChapterEntity?>,
    visibilityState: MutableState<VisibilityState>,
) {
    val label = selectedChapter.value?.label ?: stringResource(R.string.no_selection)

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Chapter")
        CustomPopupOpenButton(
            label = label,
            enabled = false,
            onClick = { visibilityState.value = VisibilityState.VISIBLE }
        )
    }
}

@Composable
fun SelectChapterPopup(
    visibilityState: MutableState<VisibilityState>,
    chapterEntities: State<List<ChapterEntity>>,
    categoryEntityState: MutableState<CategoryEntity>,
) {
    val onDismiss = { visibilityState.value = VisibilityState.HIDDEN }

    SelectionDialog(
        title = { Text(text = "Select Chapter") },
        content = {
            LazyColumn {
                item {
                    SelectChapterPopupRow(null, onDismiss)
                }
                items(chapterEntities.value) {
                    SelectChapterPopupRow(it, onSelect = {
                        categoryEntityState.value = categoryEntityState.value.copy(chapterId = it.id)
                        onDismiss()
                    })
                }
            }
        },
        modifier = Modifier.padding(horizontal = 0.dp),
        onDismiss = onDismiss,
    )
}

@Composable
fun SelectChapterPopupRow(
    chapterEntity: ChapterEntity?,
    onSelect: () -> Unit,
) {
    Row(modifier = Modifier
        .height(56.dp)
        .fillMaxWidth()
        .clickable {
            onSelect()
        }) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            val chapterLabel = chapterEntity?.label ?: stringResource(R.string.no_selection)
            Text(text = chapterLabel, modifier = Modifier.fillMaxWidth())
        }
    }
}

// endregion

// region Sub Category Row

@Composable
fun SelectSubCategoryEntitiesRow(
    selectedSubCategoryEntities: State<List<SubCategoryEntity>>,
    newSubCategoryEntities: SnapshotStateList<SubCategoryEntity>,
    onDelete: (Any) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Sub Categories")
        SubCategoryEntitiesColumn(selectedSubCategoryEntities, newSubCategoryEntities, onDelete)
    }
}

@Composable
fun SubCategoryEntitiesColumn(
    selectedSubCategoryEntities: State<List<SubCategoryEntity>>,
    newSubCategoryEntities: SnapshotStateList<SubCategoryEntity>,
    onDelete: (Any) -> Unit,
) {
    val addSubCategoryVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }

    Column {
        selectedSubCategoryEntities.value.forEach {
            SubCategoryEntityRow(subCategoryEntity = it, onDelete)
        }
        newSubCategoryEntities.forEach {
            SubCategoryEntityRow(subCategoryEntity = it, onDelete = { any ->
                when (any) {
                    is SubCategoryEntity -> newSubCategoryEntities.remove(it)
                }
            })
        }
        AddMoreSubCategoryRow(addSubCategoryVisibilityState)
    }

    ControlCreateSubCategoryEntityDialog(addSubCategoryVisibilityState, onCreate = { newSubCategoryEntities.add(it) })
}

@Composable
fun SubCategoryEntityRow(
    subCategoryEntity: SubCategoryEntity,
    onDelete: (Any) -> Unit,
) {
    val removeSubCategoryDialogState = remember { mutableStateOf(VisibilityState.HIDDEN) }

    Column {
        Row {
            Text(text = subCategoryEntity.label)
            IconButton(onClick = { removeSubCategoryDialogState.value = VisibilityState.VISIBLE }) {
                Icon(Icons.Default.Edit, "Edit Sub-Category")
            }
        }
        HorizontalDivider(thickness = 1.dp)
    }

    ControlConfirmDeleteDialog(removeSubCategoryDialogState, subCategoryEntity, onDelete)
}

@Composable
fun AddMoreSubCategoryRow(visibilityState: MutableState<VisibilityState>) {
    Row {
        Button(onClick = { visibilityState.value = VisibilityState.VISIBLE }) {
            Text(text = "Add More")
        }
    }
}

@Composable
fun ControlConfirmDeleteDialog(
    visibilityState: MutableState<VisibilityState>,
    subCategoryEntity: SubCategoryEntity,
    onDelete: (Any) -> Unit,
) {
    when (visibilityState.value) {
        VisibilityState.VISIBLE -> {
            ConfirmationDialog(title = { Text("Confirm Remove Sub-Category") },
                text = { Text(text = "Are you sure you want to remove Sub-Category: ${subCategoryEntity.label}?") },
                onConfirm = {
                    onDelete(subCategoryEntity)
                    visibilityState.value = VisibilityState.HIDDEN
                },
                onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
        }

        VisibilityState.HIDDEN -> {
            /* Do Nothing */
        }
    }
}

@Composable
fun ControlCreateSubCategoryEntityDialog(
    visibilityState: MutableState<VisibilityState>, onCreate: (SubCategoryEntity) -> Unit
) {
    when (visibilityState.value) {
        VisibilityState.VISIBLE -> {
            UpdateSubCategoryEntityDialog(onCreate = {
                onCreate(it)
                visibilityState.value = VisibilityState.HIDDEN
            }, onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
        }

        VisibilityState.HIDDEN -> {/* Do Nothing */
        }
    }
}

@Composable
fun UpdateSubCategoryEntityDialog(onCreate: (SubCategoryEntity) -> Unit, onDismiss: () -> Unit) {
    val label = remember { mutableStateOf("") }
    CreateDialog(title = { Text("Create Sub Category") }, content = {
        CustomTextField(value = label.value, label = "Label", onValueChange = { label.value = it })
    }, onConfirm = {
        onCreate(SubCategoryEntity(label = label.value))
    }, onDismiss = onDismiss
    )
}

// endregion

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

// region Create/Update Button

@Composable
fun CategoryUpdateButtonsRow(
    isEntityProvided: Boolean,
    categoryEntityState: MutableState<CategoryEntity>,
    newSubCategoryEntities: SnapshotStateList<SubCategoryEntity>,
    onCreate: (Any) -> Unit,
    onUpdate: (Any) -> Unit,
    onDelete: (Any) -> Unit,
    onDismiss: () -> Unit,
) {
    Row {
        Button(enabled = categoryEntityState.value.label.isNotBlank(), onClick = {

            if (isEntityProvided) onUpdate(categoryEntityState.value) else onCreate(categoryEntityState.value)

            newSubCategoryEntities.forEach {
                onCreate(SubCategoryEntity(label = it.label, parentCategoryId = categoryEntityState.value.id))
            }
            newSubCategoryEntities.clear()

            onDismiss()
        }) {
            val buttonLabel = stringResource(if (isEntityProvided) R.string.update else R.string.create)
            Text(text = buttonLabel)
        }
        Button(onClick = { onDismiss() }) {
            Text(text = "Cancel")
        }
        if (isEntityProvided) {
            Button(onClick = {
                onDelete(categoryEntityState.value)
                onDismiss()
            }) {
                Text(text = "Delete")
            }
        }
    }
}

//endregion
