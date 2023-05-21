package com.nishitnagar.splitnish.ui.creator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.ui.composable.ConfirmationDialog
import com.nishitnagar.splitnish.ui.composable.CreateDialog
import com.nishitnagar.splitnish.ui.composable.CustomPopupOpenButton
import com.nishitnagar.splitnish.ui.composable.CustomTextField
import java.util.UUID

private const val ID = "id"
private const val LABEL = "label"
private const val CHAPTER = "chapter"
private const val SUB_CATEGORY_ENTITIES = "subCategoryEntities"
private const val NEW_SUB_CATEGORY_ENTITIES = "newSubCategoryEntities"

@Composable
fun UpdateCategoryComposable(
    providedEntity: CategoryEntity?,
    chapterEntities: State<List<ChapterEntity>>,
    subCategoryEntities: State<List<SubCategoryEntity>>,
    onCreate: (Any) -> Unit,
    onUpdate: (Any) -> Unit,
    onDelete: (Any) -> Unit,
    onDismiss: () -> Unit
) {
    val categoryEntityState = remember { mutableStateOf(providedEntity ?: CategoryEntity(label = "")) }
    val dataMap = loadProvidedEntity(providedEntity, chapterEntities, subCategoryEntities)
    val labelState = dataMap[LABEL] as MutableState<String>
    val selectedChapter = dataMap[CHAPTER] as MutableState<ChapterEntity?>
    val selectedSubCategoryEntities = dataMap[SUB_CATEGORY_ENTITIES] as MutableState<List<SubCategoryEntity>>

    val newSubCategoryEntities = remember { mutableStateListOf<SubCategoryEntity>() }
    dataMap[NEW_SUB_CATEGORY_ENTITIES] = newSubCategoryEntities

    val selectChapterPopupVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }

    Column {
        Text(text = "Create ${stringResource(R.string.category_entity)}")
        LazyColumn {
            item { SelectLabelRow(categoryEntityState) }
            item { SelectChapterRow(selectedChapter, selectChapterPopupVisibilityState) }
            item { SelectSubCategoryEntitiesRow(selectedSubCategoryEntities, newSubCategoryEntities, onDelete) }
        }
        ButtonsRow(dataMap = dataMap, onCreate = onCreate, onUpdate = onUpdate, onDismiss = onDismiss)
    }

    ControlSelectPopup(visibilityState = selectChapterPopupVisibilityState) {
        SelectChapterPopup(
            visibilityState = selectChapterPopupVisibilityState,
            chapterEntities = chapterEntities,
            selectedChapter = selectedChapter,
        )
    }
}

@Composable
fun loadProvidedEntity(
    providedEntity: CategoryEntity?,
    chapterEntities: State<List<ChapterEntity>>,
    subCategoryEntities: State<List<SubCategoryEntity>>,
): MutableMap<String, Any> {
    val dataMap = mutableMapOf<String, Any>()

    dataMap[ID] = UUID.randomUUID()
    dataMap[LABEL] = remember { mutableStateOf("") }
    dataMap[CHAPTER] = remember { mutableStateOf<ChapterEntity?>(null) }
    dataMap[SUB_CATEGORY_ENTITIES] = remember { mutableStateOf<List<SubCategoryEntity>>(emptyList()) }

    if (providedEntity != null) {
        dataMap[ID] = providedEntity.id

        (dataMap[LABEL] as MutableState<String>).value = providedEntity.label

        if (providedEntity.chapterId != null) {
            (dataMap[CHAPTER] as MutableState<ChapterEntity?>).value =
                chapterEntities.value.first { it.id == providedEntity.chapterId }
        }

        (dataMap[SUB_CATEGORY_ENTITIES] as MutableState<List<SubCategoryEntity>>).value =
            subCategoryEntities.value.filter { it.parentCategoryId == providedEntity.id }
    }

    return dataMap
}

// region Label Row

@Composable
fun SelectLabelRow(
    categoryEntityState: MutableState<CategoryEntity>,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Label a${categoryEntityState.value.label}")
        CustomTextField(value = categoryEntityState.value.label, label = "",
            onValueChange = { categoryEntityState.value = categoryEntityState.value.copy(label = it) })
    }
}

// endregion

// region Chapter Row

@Composable
fun SelectChapterRow(
    selectedChapter: MutableState<ChapterEntity?>,
    visibilityState: MutableState<VisibilityState>,
) {
    val label = selectedChapter.value?.label ?: "CHAPTER"

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Chapter")
        CustomPopupOpenButton(label = label, onClick = { visibilityState.value = VisibilityState.VISIBLE })
    }
}

@Composable
fun SelectChapterPopup(
    visibilityState: MutableState<VisibilityState>,
    chapterEntities: State<List<ChapterEntity>>,
    selectedChapter: MutableState<ChapterEntity?>,
) {
    val onDismiss = { visibilityState.value = VisibilityState.HIDDEN }

    SelectionDialog(
        title = { Text(text = "Select Chapter") },
        content = {
            LazyColumn {
                item {
                    SelectChapterPopupRow(null, selectedChapter, onDismiss)
                }
                items(chapterEntities.value) {
                    SelectChapterPopupRow(it, selectedChapter, onDismiss)
                }
            }
        },
        modifier = Modifier.padding(horizontal = 0.dp),
        onDismiss = onDismiss,
    )
}

@Composable
fun SelectChapterPopupRow(
    chapterEntity: ChapterEntity?, selectedChapter: MutableState<ChapterEntity?>, onDismiss: () -> Unit
) {
    Row(modifier = Modifier
        .height(56.dp)
        .fillMaxWidth()
        .clickable {
            selectedChapter.value = chapterEntity
            onDismiss()
        }) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            val chapterLabel = chapterEntity?.label ?: "CHAPTER"
            Text(text = chapterLabel, modifier = Modifier.fillMaxWidth())
        }
    }
}

// endregion

// region Sub Category Row

@Composable
fun SelectSubCategoryEntitiesRow(
    selectedSubCategoryEntities: MutableState<List<SubCategoryEntity>>,
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
    selectedSubCategoryEntities: MutableState<List<SubCategoryEntity>>,
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
        AddMoreRow(addSubCategoryVisibilityState)
    }

    ControlCreateSubCategoryEntityDialog(addSubCategoryVisibilityState, onCreate = { newSubCategoryEntities.add(it) })
}

@Composable
fun SubCategoryEntityRow(
    subCategoryEntity: SubCategoryEntity,
    onDelete: (Any) -> Unit,
) {
    val removePlayerPopupState = remember { mutableStateOf(VisibilityState.HIDDEN) }

    Column {
        Row {
            Text(text = subCategoryEntity.label)
            IconButton(onClick = { removePlayerPopupState.value = VisibilityState.VISIBLE }) {
                Icon(Icons.Default.Delete, "Remove Sub-Category")
            }
        }
        Divider(thickness = 1.dp)
    }

    ControlConfirmDeleteDialog(removePlayerPopupState, subCategoryEntity, onDelete)
}

@Composable
fun AddMoreRow(visibilityState: MutableState<VisibilityState>) {
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

        VisibilityState.HIDDEN -> {/* Do Nothing */
        }
    }
}

@Composable
fun ControlCreateSubCategoryEntityDialog(
    visibilityState: MutableState<VisibilityState>, onCreate: (SubCategoryEntity) -> Unit
) {
    when (visibilityState.value) {
        VisibilityState.VISIBLE -> {
            CreateSubCategoryEntityDialog(onCreate = {
                onCreate(it)
                visibilityState.value = VisibilityState.HIDDEN
            }, onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
        }

        VisibilityState.HIDDEN -> {/* Do Nothing */
        }
    }
}

@Composable
fun CreateSubCategoryEntityDialog(onCreate: (SubCategoryEntity) -> Unit, onDismiss: () -> Unit) {
    var label = ""
    CreateDialog(title = { Text("Create Sub Category") }, content = {
        CustomTextField(value = label, label = "Label", onValueChange = { label = it })
    }, onConfirm = {
        onCreate(SubCategoryEntity(label = label))
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

// region Create Button

@Composable
fun ButtonsRow(
    dataMap: MutableMap<String, Any>, onCreate: (Any) -> Unit, onUpdate: (Any) -> Unit, onDismiss: () -> Unit
) {
    Row {
        val label = (dataMap[LABEL] as MutableState<String>).value

        Button(enabled = label.isNotBlank(), onClick = {
            val id = dataMap[ID] as UUID
            val chapter = (dataMap[CHAPTER] as MutableState<ChapterEntity?>).value
            val newSubCategoryEntities = (dataMap[NEW_SUB_CATEGORY_ENTITIES] as SnapshotStateList<SubCategoryEntity>)

            val categoryEntity = CategoryEntity(id = id, label = label, chapterId = chapter?.id)

            onCreate(categoryEntity)
            newSubCategoryEntities.forEach { onCreate(SubCategoryEntity(label = it.label, parentCategoryId = id)) }
            newSubCategoryEntities.clear()

            onDismiss()
        }) {
            Text(text = "Create")
        }
        Button(onClick = { onDismiss() }) {
            Text(text = "Cancel")
        }
    }
}

//endregion
