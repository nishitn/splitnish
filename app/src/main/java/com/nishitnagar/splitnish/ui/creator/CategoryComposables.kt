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

class CategoryComposables {
  companion object {
    @Composable
    fun Update(
      providedEntity: CategoryEntity?,
      entityData: Serializable?,
      chapterEntities: State<List<ChapterEntity>>,
      subCategoryEntities: State<List<SubCategoryEntity>>,
      onCreate: (Any) -> Unit,
      onUpdate: (Any) -> Unit,
      onDelete: (Any) -> Unit,
      onDismiss: () -> Unit,
    ) {
      val uuidString = Helper.getKeyFromSerializedMap(entityData, BundleKeys.CHAPTER_ID.name)
      val selectedChapterId = if (uuidString != null) UUID.fromString(uuidString) else null
      val categoryEntityState =
        remember { mutableStateOf(CategoryEntity(label = "", chapterId = selectedChapterId)) }
      if (providedEntity != null) categoryEntityState.value = providedEntity

      val selectedChapter = remember {
        derivedStateOf {
          chapterEntities.value.firstOrNull { it.id == categoryEntityState.value.chapterId }
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
          item { LabelRow(categoryEntityState) }
          item { ParentChapterRow(selectedChapter, selectChapterPopupVisibilityState) }
          item {
            SubCategoryEntitiesRow(
              selectedSubCategoryEntities,
              newSubCategoryEntities,
              onDelete
            )
          }
        }
        ButtonsRow(
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
    private fun LabelRow(
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

    // region Parent Chapter Row

    @Composable
    private fun ParentChapterRow(
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
          onClick = { visibilityState.value = VisibilityState.VISIBLE }
        )
      }
    }

    @Composable
    private fun SelectChapterPopup(
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
              SelectChapterPopupRow(null, onSelect = {
                categoryEntityState.value = categoryEntityState.value.copy(chapterId = null)
                onDismiss()
              })
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
    private fun SelectChapterPopupRow(
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
    private fun SubCategoryEntitiesRow(
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
    private fun SubCategoryEntitiesColumn(
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

      ControlCreateSubCategoryEntityDialog(
        addSubCategoryVisibilityState,
        onCreate = { newSubCategoryEntities.add(it) })
    }

    @Composable
    private fun SubCategoryEntityRow(
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
    private fun AddMoreSubCategoryRow(visibilityState: MutableState<VisibilityState>) {
      Row {
        Button(onClick = { visibilityState.value = VisibilityState.VISIBLE }) {
          Text(text = "Add More")
        }
      }
    }

    @Composable
    private fun ControlConfirmDeleteDialog(
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
    private fun ControlCreateSubCategoryEntityDialog(
      visibilityState: MutableState<VisibilityState>, onCreate: (SubCategoryEntity) -> Unit,
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
    private fun UpdateSubCategoryEntityDialog(
      onCreate: (SubCategoryEntity) -> Unit,
      onDismiss: () -> Unit,
    ) {
      val label = remember { mutableStateOf("") }
      CreateDialog(title = { Text("Create Sub Category") }, content = {
        CustomTextField(
          value = label.value,
          label = "Label",
          onValueChange = { label.value = it })
      }, onConfirm = {
        onCreate(SubCategoryEntity(label = label.value))
      }, onDismiss = onDismiss
      )
    }

    // endregion

    // region Buttons Row

    @Composable
    fun ButtonsRow(
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

          if (isEntityProvided) onUpdate(categoryEntityState.value) else onCreate(
            categoryEntityState.value
          )

          newSubCategoryEntities.forEach {
            onCreate(
              SubCategoryEntity(
                label = it.label,
                parentCategoryId = categoryEntityState.value.id
              )
            )
          }
          newSubCategoryEntities.clear()

          onDismiss()
        }) {
          val buttonLabel =
            stringResource(if (isEntityProvided) R.string.update else R.string.create)
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
  }
}