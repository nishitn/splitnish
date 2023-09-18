package com.nishitnagar.splitnish.ui.creator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.nishitnagar.splitnish.R
import com.nishitnagar.splitnish.data.entity.ChapterEntity

@Composable
fun CreateChapterComposable(
    onCreate: (Any) -> Unit, onDismiss: () -> Unit
) {
    val chapterEntityState = remember { mutableStateOf(ChapterEntity(label = "")) }

    Column {
        Text(text = "${stringResource(R.string.create)} ${stringResource(R.string.chapter_entity)}")
        LazyColumn {
            item { SelectChapterLabelRow(chapterEntityState) }
        }
        ChapterCreateButtonsRow(
            chapterEntityState = chapterEntityState,
            onCreate = onCreate,
            onDismiss = onDismiss,
        )
    }
}

// region Create Button

@Composable
fun ChapterCreateButtonsRow(
    chapterEntityState: MutableState<ChapterEntity>,
    onCreate: (Any) -> Unit,
    onDismiss: () -> Unit,
) {
    Row {
        Button(enabled = chapterEntityState.value.label.isNotBlank(), onClick = {
            onCreate(chapterEntityState.value)
            onDismiss()
        }) {
            val buttonLabel = stringResource(R.string.create)
            Text(text = buttonLabel)
        }
        Button(onClick = { onDismiss() }) {
            Text(text = stringResource(R.string.cancel))
        }
    }
}

//endregion
