package com.nishitnagar.splitnish

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.nishitnagar.splitnish.enums.BundleKeys
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.ui.composable.ControlShowDataComposable
import com.nishitnagar.splitnish.ui.theme.SplitnishTheme
import com.nishitnagar.splitnish.util.Helper
import com.nishitnagar.splitnish.viewmodel.TransactionViewModel
import com.nishitnagar.splitnish.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transactionViewModel: TransactionViewModel by viewModels()
        val userViewModel: UserViewModel by viewModels()
        Helper.activeUserEntity = userViewModel.getActiveUserEntity()

        setContent {
            SplitnishTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen(transactionViewModel = transactionViewModel, userViewModel = userViewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(transactionViewModel: TransactionViewModel, userViewModel: UserViewModel) {
    val accountEntities = transactionViewModel.accountEntities.collectAsState(initial = listOf())
    val categoryEntities = transactionViewModel.categoryEntities.collectAsState(initial = listOf())
    val subCategoryEntities = transactionViewModel.subCategoryEntities.collectAsState(initial = listOf())
    val chapterEntities = transactionViewModel.chapterEntities.collectAsState(initial = listOf())
    val transactionEntities = transactionViewModel.transactionEntities.collectAsState(initial = listOf())
    val userEntities = userViewModel.userEntities.collectAsState(initial = listOf())

    val categories = transactionViewModel.categories.collectAsState(initial = listOf())
    val chapters = transactionViewModel.categories.collectAsState(initial = listOf())
    val transactions = transactionViewModel.transactions.collectAsState(initial = listOf())

    val accountEntitiesVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val categoryEntitiesVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val subCategoryEntitiesVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val chapterEntitiesVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val transactionEntitiesVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val userEntitiesVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }

    val categoryVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val chapterVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val transactionVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }

    Column {
        Button(onClick = { switch(accountEntitiesVisibilityState) }) {
            Text(stringResource(R.string.account_entity))
        }
        Button(onClick = { switch(categoryEntitiesVisibilityState) }) {
            Text(stringResource(R.string.category_entity))
        }
        Button(onClick = { switch(subCategoryEntitiesVisibilityState) }) {
            Text(stringResource(R.string.sub_category_entity))
        }
        Button(onClick = { switch(chapterEntitiesVisibilityState) }) {
            Text(stringResource(R.string.chapter_entity))
        }
        Button(onClick = { switch(userEntitiesVisibilityState) }) {
            Text(stringResource(R.string.user_entity))
        }
        Button(onClick = { switch(transactionEntitiesVisibilityState) }) {
            Text(stringResource(R.string.transaction_entity))
        }

        Button(onClick = { switch(categoryVisibilityState) }) {
            Text(stringResource(R.string.category))
        }
        Button(onClick = { switch(chapterVisibilityState) }) {
            Text(stringResource(R.string.chapter))
        }
        Button(onClick = { switch(transactionVisibilityState) }) {
            Text(stringResource(R.string.transaction))
        }


        val context = LocalContext.current

        ControlShowDataComposable(visibilityState = accountEntitiesVisibilityState,
            entities = accountEntities,
            resourceId = R.string.account_entity,
            onClick = {
                val intent = Intent(context, CreatorActivity::class.java)
                if(it != null) intent.putExtra(BundleKeys.ENTITY_ID.name, it.toString())
                intent.putExtra(BundleKeys.INT_RESOURCE.name, R.string.account_entity)
                context.startActivity(intent)
            })
        ControlShowDataComposable(visibilityState = categoryEntitiesVisibilityState,
            entities = categoryEntities,
            resourceId = R.string.category_entity,
            onClick = {
                val intent = Intent(context, CreatorActivity::class.java)
                if(it != null) intent.putExtra(BundleKeys.ENTITY_ID.name, it.toString())
                intent.putExtra(BundleKeys.INT_RESOURCE.name, R.string.category_entity)
                context.startActivity(intent)
            })
        ControlShowDataComposable(visibilityState = subCategoryEntitiesVisibilityState,
            entities = subCategoryEntities,
            resourceId = R.string.sub_category_entity,
            onClick = {
                val intent = Intent(context, CreatorActivity::class.java)
                if(it != null) intent.putExtra(BundleKeys.ENTITY_ID.name, it.toString())
                intent.putExtra(BundleKeys.INT_RESOURCE.name, R.string.sub_category_entity)
                context.startActivity(intent)
            })
        ControlShowDataComposable(visibilityState = chapterEntitiesVisibilityState,
            entities = chapterEntities,
            resourceId = R.string.chapter_entity,
            onClick = {
                val intent = Intent(context, CreatorActivity::class.java)
                if(it != null) intent.putExtra(BundleKeys.ENTITY_ID.name, it.toString())
                intent.putExtra(BundleKeys.INT_RESOURCE.name, R.string.chapter_entity)
                context.startActivity(intent)
            })
        ControlShowDataComposable(visibilityState = transactionEntitiesVisibilityState,
            entities = transactionEntities,
            resourceId = R.string.transaction_entity,
            onClick = {
                val intent = Intent(context, CreatorActivity::class.java)
                if(it != null) intent.putExtra(BundleKeys.ENTITY_ID.name, it.toString())
                intent.putExtra(BundleKeys.INT_RESOURCE.name, R.string.transaction_entity)
                context.startActivity(intent)
            })
        ControlShowDataComposable(visibilityState = userEntitiesVisibilityState,
            entities = userEntities,
            resourceId = R.string.user_entity,
            onClick = {
                val intent = Intent(context, CreatorActivity::class.java)
                if(it != null) intent.putExtra(BundleKeys.ENTITY_ID.name, it.toString())
                intent.putExtra(BundleKeys.INT_RESOURCE.name, R.string.user_entity)
                context.startActivity(intent)
            })

        ControlShowDataComposable(visibilityState = categoryVisibilityState,
            entities = categories,
            resourceId = R.string.category,
            onClick = {
                val intent = Intent(context, CreatorActivity::class.java)
                if(it != null) intent.putExtra(BundleKeys.ENTITY_ID.name, it.toString())
                intent.putExtra(BundleKeys.INT_RESOURCE.name, R.string.category)
                context.startActivity(intent)
            })
        ControlShowDataComposable(visibilityState = chapterVisibilityState,
            entities = chapters,
            resourceId = R.string.chapter,
            onClick = {
                val intent = Intent(context, CreatorActivity::class.java)
                if(it != null) intent.putExtra(BundleKeys.ENTITY_ID.name, it.toString())
                intent.putExtra(BundleKeys.INT_RESOURCE.name, R.string.chapter)
                context.startActivity(intent)
            })
        ControlShowDataComposable(visibilityState = transactionVisibilityState,
            entities = transactions,
            resourceId = R.string.transaction,
            onClick = {
                val intent = Intent(context, CreatorActivity::class.java)
                if(it != null) intent.putExtra(BundleKeys.ENTITY_ID.name, it.toString())
                intent.putExtra(BundleKeys.INT_RESOURCE.name, R.string.transaction)
                context.startActivity(intent)
            })
    }
}

fun switch(visibilityState: MutableState<VisibilityState>) {
    visibilityState.value = if (visibilityState.value == VisibilityState.VISIBLE) VisibilityState.HIDDEN
    else VisibilityState.VISIBLE
}