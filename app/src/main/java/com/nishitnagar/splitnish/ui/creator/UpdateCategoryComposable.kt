package com.nishitnagar.splitnish.ui.creator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.data.entity.ChapterEntity
import com.nishitnagar.splitnish.data.model.Category
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.ui.composable.CustomPopupOpenButton
import com.nishitnagar.splitnish.ui.composable.CustomTextField
import com.nishitnagar.splitnish.util.Helper
import java.util.*

private const val LABEL = "label"
private const val CHAPTER = "chapter"
private const val PARENT_CATEGORY = "parentCategory"

@Composable
fun UpdateCategoryComposable(
    providedEntity: Category?,
    chapterEntities: State<List<ChapterEntity>>,
    categories: State<List<Category>>,
    onCreate: (CategoryEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val dataMap = loadProvidedEntity(providedEntity, chapterEntities, categories)
    val labelState = dataMap[LABEL] as MutableState<String>
    val selectedChapter = dataMap[CHAPTER] as MutableState<ChapterEntity?>
    val selectedCategory = dataMap[PARENT_CATEGORY] as MutableState<CategoryEntity?>

    val selectChapterPopupVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val selectParentPopupVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }

    Column {
        LazyColumn {
            item { SelectLabelRow(labelState) }
            item { SelectChapterRow(selectedChapter, selectChapterPopupVisibilityState) }
            item { SelectParentCategoryRow(selectedCategory, selectParentPopupVisibilityState, categories) }
        }
        ButtonsRow(dataMap = dataMap, onCreate = onCreate, onDismiss = onDismiss)
    }

    ControlSelectPopup(visibilityState = selectChapterPopupVisibilityState) {
        SelectChapterPopup(
            visibilityState = selectChapterPopupVisibilityState,
            chapterEntities = chapterEntities,
            selectedChapter = selectedChapter,
        )
    }

    ControlSelectPopup(visibilityState = selectParentPopupVisibilityState) {
        SelectCategoryPopup(
            visibilityState = selectParentPopupVisibilityState,
            allCategories = categories,
            selectedCategory = selectedCategory,
        )
    }
}

@Composable
fun loadProvidedEntity(
    providedEntity: Category?,
    chapterEntities: State<List<ChapterEntity>>,
    categories: State<List<Category>>,
): MutableMap<String, Any> {
    val dataMap = mutableMapOf<String, Any>()

    dataMap[LABEL] = remember { mutableStateOf("") }
    dataMap[CHAPTER] = remember { mutableStateOf<ChapterEntity?>(null) }
    dataMap[PARENT_CATEGORY] = remember { mutableStateOf<Category?>(null) }

    if (providedEntity != null) {
        (dataMap[LABEL] as MutableState<String>).value = providedEntity.categoryEntity.label

        (dataMap[CHAPTER] as MutableState<ChapterEntity?>).value =
            chapterEntities.value.first { it.id == providedEntity.categoryEntity.chapterId }

        (dataMap[PARENT_CATEGORY] as MutableState<Category?>).value =
            categories.value.first { it.categoryEntity.id == providedEntity.categoryEntity.parentCategoryId }
    }

    return dataMap
}

// region Label Row

@Composable
fun SelectLabelRow(
    labelState: MutableState<String>,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Label")
        CustomTextField(value = labelState.value, label = "", onValueChange = { labelState.value = it })
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

// region Parent Category Row

@Composable
fun SelectParentCategoryRow(
    selectedCategory: MutableState<CategoryEntity?>,
    visibilityState: MutableState<VisibilityState>,
    allCategories: State<List<Category>>,
) {
    val label = if (selectedCategory.value != null) Helper.getCategoryText(selectedCategory.value!!.id, allCategories)
    else "CATEGORY"

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Parent Category")
        CustomPopupOpenButton(label = label, onClick = { visibilityState.value = VisibilityState.VISIBLE })
    }
}

@Composable
fun SelectCategoryPopup(
    visibilityState: MutableState<VisibilityState>,
    allCategories: State<List<Category>>,
    selectedCategory: MutableState<CategoryEntity?>,
) {
    val onDismiss = { visibilityState.value = VisibilityState.HIDDEN }
    val parentCategoryId = remember { mutableStateOf(selectedCategory.value?.parentCategoryId) }
    val categoriesToShow = remember {
        mutableStateOf(allCategories.value.filter { it.categoryEntity.parentCategoryId == parentCategoryId.value }
            .map { it.categoryEntity }.toList<CategoryEntity?>())
    }

    SelectionDialog(
        title = { Text(text = "Select Category") },
        content = {
            LazyColumn {
                item {
                    GoBackCategoryPopupRow(parentCategoryId, categoriesToShow, allCategories, onDismiss)
                }
                items(categoriesToShow.value) {
                    SelectCategoryPopupRow(
                        it, selectedCategory, categoriesToShow, parentCategoryId, allCategories, onDismiss
                    )
                }
            }
        },
        modifier = Modifier.padding(horizontal = 0.dp),
        onDismiss = onDismiss,
    )
}

@Composable
fun SelectCategoryPopupRow(
    categoryEntity: CategoryEntity?,
    selectedCategory: MutableState<CategoryEntity?>,
    categoriesToShow: MutableState<List<CategoryEntity?>>,
    parentCategoryId: MutableState<UUID?>,
    allCategories: State<List<Category>>,
    onDismiss: () -> Unit
) {
    val category: Category?
    val categoryLabel: String

    if (categoryEntity != null) {
        category = allCategories.value.firstOrNull { it.categoryEntity.id == categoryEntity.id }
        categoryLabel = Helper.getCategoryText(categoryEntity.id, allCategories)
    } else {
        category = null
        categoryLabel = "CATEGORY"
    }

    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .clickable {
                selectedCategory.value = categoryEntity
                onDismiss()
            }) {
            Text(text = categoryLabel, modifier = Modifier.fillMaxWidth())
        }
        if (category != null && category.subCategoryEntities.isNotEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .width(48.dp)
                .clickable {
                    parentCategoryId.value = category.categoryEntity.id
                    categoriesToShow.value = category.subCategoryEntities
                }) {
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Expand",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun GoBackCategoryPopupRow(
    parentCategoryId: MutableState<UUID?>,
    categoriesToShow: MutableState<List<CategoryEntity?>>,
    allCategories: State<List<Category>>,
    onDismiss: () -> Unit,
) {
    val label = if (parentCategoryId.value == null) "NONE" else ".."

    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .clickable {
                if (parentCategoryId.value != null) {
                    parentCategoryId.value = allCategories.value
                        .filter { it.categoryEntity.id == parentCategoryId.value }
                        .map { it.categoryEntity.parentCategoryId }
                        .firstOrNull()

                    categoriesToShow.value = allCategories.value
                        .filter { it.categoryEntity.parentCategoryId == parentCategoryId.value }
                        .map { it.categoryEntity }
                } else {
                    onDismiss()
                }
            }) {
            Text(text = label, modifier = Modifier.fillMaxWidth(), fontStyle = FontStyle.Italic)
        }
    }
}

// endregion

// region Create Button

@Composable
fun ButtonsRow(
    dataMap: MutableMap<String, Any>, onCreate: (CategoryEntity) -> Unit,
    onDismiss: () -> Unit
) {
    Row {
        val label = (dataMap[LABEL] as MutableState<String>).value

        Button(enabled = label.isNotBlank(), onClick = {
            val chapter = (dataMap[CHAPTER] as MutableState<ChapterEntity?>).value
            val parentCategory = (dataMap[PARENT_CATEGORY] as MutableState<CategoryEntity?>).value

            val categoryEntity = CategoryEntity(
                label = label, chapterId = chapter?.id, parentCategoryId = parentCategory?.id
            )
            onCreate(categoryEntity)
        }) {
            Text(text = "Create")
        }
        Button(onClick = { onDismiss() }) {
            Text(text = "Cancel")
        }
    }
}

//endregion
