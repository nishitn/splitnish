package com.nishitnagar.splitnish.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nishitnagar.splitnish.data.entity.AccountEntity
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.data.entity.ChapterEntity
import com.nishitnagar.splitnish.data.entity.SubCategoryEntity
import com.nishitnagar.splitnish.data.entity.TransactionEntity
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.util.Helper
import java.util.UUID

@Composable
fun ControlShowDataComposable(
    visibilityState: MutableState<VisibilityState>,
    entities: State<List<Any>>,
    resourceId: Int,
    onClick: (UUID?) -> Unit
) {
    when (visibilityState.value) {
        VisibilityState.VISIBLE -> {
            ShowDataComposable(entities = entities,
                resourceId = resourceId,
                onClick = onClick,
                onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
        }

        VisibilityState.HIDDEN -> {/* Do Nothing */
        }
    }
}

@Composable
fun ShowDataComposable(
    entities: State<List<Any>>, resourceId: Int, onClick: (UUID?) -> Unit, onDismiss: () -> Unit
) {
    Column {
        Row {
            Button(onClick = { onClick(null) }) {
                Text("Create")
            }
            Button(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
        ShowDataLazyColumn(entities = entities, resourceId = resourceId, onClick)
    }
}

@Composable
fun ShowDataLazyColumn(entities: State<List<Any>>, resourceId: Int, onClick: (UUID?) -> Unit) {
    LazyColumn {
        item {
            HeadingText(text = stringResource(resourceId))
        }
        items(entities.value) { item ->
            Text(text = Helper.gson.toJson(item), modifier = Modifier
                .padding(8.dp)
                .clickable {
                    when (item) {
                        is AccountEntity -> onClick(item.id)
                        is CategoryEntity -> onClick(item.id)
                        is SubCategoryEntity -> onClick(item.id)
                        is ChapterEntity -> onClick(item.id)
                        is TransactionEntity -> onClick(item.id)
                    }
                })
        }
    }
}