package com.nishitnagar.splitnish.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.nishitnagar.splitnish.data.entity.ChapterEntity
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.ui.previewprovider.ChapterEntitiesProvider
import com.nishitnagar.splitnish.util.Helper


@Composable
fun ControlChapterScreen(
    visibilityState: MutableState<VisibilityState>,
    chapterEntities: State<List<ChapterEntity>>,
    onCreate: (ChapterEntity) -> Unit,
) {
  when (visibilityState.value) {
    VisibilityState.VISIBLE -> {
      ChapterScreen(chapterEntities = chapterEntities,
                    onCreate = onCreate,
                    onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
    }

    VisibilityState.HIDDEN -> {
      /* Do Nothing */
    }
  }
}

@Composable
fun ChapterScreen(
    chapterEntities: State<List<ChapterEntity>>,
    onCreate: (ChapterEntity) -> Unit,
    onDismiss: () -> Unit,
) {
  val dialogVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
  Column {
    Row {
      Button(onClick = { dialogVisibilityState.value = VisibilityState.VISIBLE }) {
        Text("Create Chapter")
      }
      Button(onClick = onDismiss) {
        Text("Dismiss")
      }
    }
    ChapterLazyColumn(chapterEntities = chapterEntities)
  }

  ControlCreateChapterEntityDialog(dialogVisibilityState, onCreate = onCreate)
}

@Composable
fun ChapterLazyColumn(
    @PreviewParameter(ChapterEntitiesProvider::class) chapterEntities: State<List<ChapterEntity>>,
) {
  LazyColumn {
    item {
      HeadingText(text = "Chapter Entities")
    }
    items(chapterEntities.value) { chapterEntity ->
      ChapterRow(chapterEntity)
    }
  }
}

@Composable
fun ChapterRow(item: ChapterEntity) {
  Text(text = Helper.gson.toJson(item), modifier = Modifier.padding(8.dp))
}

@Composable
fun ControlCreateChapterEntityDialog(
    visibilityState: MutableState<VisibilityState>, onCreate: (ChapterEntity) -> Unit,
) {
  when (visibilityState.value) {
    VisibilityState.VISIBLE -> {
      CreateChapterDialog(onCreate = {
        onCreate(it)
        visibilityState.value = VisibilityState.HIDDEN
      }, onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
    }

    VisibilityState.HIDDEN -> {
      /* Do Nothing */
    }
  }
}

@Composable
fun CreateChapterDialog(onCreate: (ChapterEntity) -> Unit, onDismiss: () -> Unit) {
  var label = ""
  CreateDialog(title = { Text("Create Chapter Entity") }, content = {
    CustomTextField(value = label, label = "Label", onValueChange = { label = it })
  }, onConfirm = { onCreate(ChapterEntity(label = label)) }, onDismiss = onDismiss)
}

@Preview(showBackground = true)
@Composable
fun PreviewChapterScreen() {
  val chapterEntities = remember {
    mutableStateOf(
      listOf(
        ChapterEntity(label = "Indore"),
        ChapterEntity(label = "Honeywell"),
        ChapterEntity(label = "Bangalore"),
      )
    )
  }
  ChapterScreen(chapterEntities = chapterEntities, onCreate = {}, onDismiss = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateChapterDialog() {
  CreateChapterDialog(onCreate = {}, onDismiss = {})
}
