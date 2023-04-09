package com.nishitnagar.splitnish.ui.composable

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.nishitnagar.splitnish.CreatorActivity
import com.nishitnagar.splitnish.data.entity.CategoryEntity
import com.nishitnagar.splitnish.data.model.Category
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.ui.previewprovider.CategoryEntitiesProvider
import com.nishitnagar.splitnish.util.Helper


@Composable
fun ControlCategoryEntityScreen(
    visibilityState: MutableState<VisibilityState>,
    categoryEntities: State<List<CategoryEntity>>,
) {
    when (visibilityState.value) {
        VisibilityState.VISIBLE -> {
            CategoryEntityScreen(categoryEntities = categoryEntities,
                onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
        }
        VisibilityState.HIDDEN -> {
            /* Do Nothing */
        }
    }
}

@Composable
fun CategoryEntityScreen(
    categoryEntities: State<List<CategoryEntity>>, onDismiss: () -> Unit
) {
    val context = LocalContext.current
    Column {
        Row {
            Button(onClick = { context.startActivity(Intent(context, CreatorActivity::class.java)) }) {
                Text("Create Category")
            }
            Button(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
        CategoryEntityLazyColumn(categoryEntities = categoryEntities)
    }
}

@Composable
fun CategoryEntityLazyColumn(@PreviewParameter(CategoryEntitiesProvider::class) categoryEntities: State<List<CategoryEntity>>) {
    LazyColumn {
        item {
            HeadingText(text = "Category Entities")
        }
        items(categoryEntities.value) { categoryEntity ->
            CategoryEntityRow(categoryEntity)
        }
    }
}

@Composable
fun CategoryEntityRow(item: CategoryEntity) {
    Text(text = Helper.gson.toJson(item), modifier = Modifier.padding(8.dp))
}

@Composable
fun ControlCategoryScreen(
    visibilityState: MutableState<VisibilityState>,
    categories: State<List<Category>>,
) {
    when (visibilityState.value) {
        VisibilityState.VISIBLE -> {
            CategoryScreen(categories = categories,
                onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
        }
        VisibilityState.HIDDEN -> {
            /* Do Nothing */
        }
    }
}

@Composable
fun CategoryScreen(
    categories: State<List<Category>>, onDismiss: () -> Unit
) {
    val context = LocalContext.current
    Column {
        Row {
            Button(onClick = { context.startActivity(Intent(context, CreatorActivity::class.java)) }) {
                Text("Create Category")
            }
            Button(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
        CategoryLazyColumn(categories = categories)
    }
}

@Composable
fun CategoryLazyColumn(categories: State<List<Category>>) {
    LazyColumn {
        item {
            HeadingText(text = "Category Entities")
        }
        items(categories.value) { category ->
            CategoryRow(category)
        }
    }
}

@Composable
fun CategoryRow(item: Category) {
    Text(text = Helper.gson.toJson(item), modifier = Modifier.padding(8.dp))
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryScreen() {
    val categoryEntities = remember {
        mutableStateOf(
            listOf(
                CategoryEntity(label = "Indore"),
                CategoryEntity(label = "Honeywell"),
                CategoryEntity(label = "Bangalore"),
            )
        )
    }
    CategoryEntityScreen(categoryEntities = categoryEntities, onDismiss = {})
}
