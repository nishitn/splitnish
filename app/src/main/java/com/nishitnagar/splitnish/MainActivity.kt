package com.nishitnagar.splitnish

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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.ui.composable.*
import com.nishitnagar.splitnish.ui.theme.SplitnishTheme
import com.nishitnagar.splitnish.util.Helper
import com.nishitnagar.splitnish.viewmodel.TransactionViewModel
import com.nishitnagar.splitnish.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

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
    val transactions = transactionViewModel.transactions.collectAsState(initial = listOf())
    val transactionEntities = transactionViewModel.transactionEntities.collectAsState(initial = listOf())
    val accountEntities = transactionViewModel.accountEntities.collectAsState(initial = listOf())
    val categoryEntities = transactionViewModel.categoryEntities.collectAsState(initial = listOf())
    val categories = transactionViewModel.categories.collectAsState(initial = listOf())
    val chapterEntities = transactionViewModel.chapterEntities.collectAsState(initial = listOf())
    val userEntities = userViewModel.userEntities.collectAsState(initial = listOf())

    val accountVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val categoryEntitiesVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val categoryVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val chapterVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val userVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }
    val transactionVisibilityState = remember { mutableStateOf(VisibilityState.HIDDEN) }

    Column {
        Button(onClick = { accountVisibilityState.value = VisibilityState.VISIBLE }) {
            Text("Account")
        }
        Button(onClick = { categoryEntitiesVisibilityState.value = VisibilityState.VISIBLE }) {
            Text("Category Entities")
        }
        Button(onClick = { categoryVisibilityState.value = VisibilityState.VISIBLE }) {
            Text("Category")
        }
        Button(onClick = { chapterVisibilityState.value = VisibilityState.VISIBLE }) {
            Text("Chapter")
        }
        Button(onClick = { userVisibilityState.value = VisibilityState.VISIBLE }) {
            Text("User")
        }
        Button(onClick = { transactionVisibilityState.value = VisibilityState.VISIBLE }) {
            Text("Transactions")
        }


        ControlAccountScreen(visibilityState = accountVisibilityState,
            accountEntities = accountEntities,
            onCreate = { transactionViewModel.insert(it) })
        ControlCategoryEntityScreen(visibilityState = categoryEntitiesVisibilityState,
            categoryEntities = categoryEntities)
        ControlCategoryScreen(visibilityState = categoryVisibilityState,
            categories = categories)
        ControlChapterScreen(visibilityState = chapterVisibilityState,
            chapterEntities = chapterEntities,
            onCreate = { transactionViewModel.insert(it) })
        ControlUserScreen(visibilityState = userVisibilityState,
            userEntities = userEntities,
            onCreate = { userViewModel.insert(it.copy(isActive = true)) })
        ControlTransactionScreen(visibilityState = transactionVisibilityState,
            transactionEntities = transactionEntities,
            onCreate = { transactionViewModel.insert(it) })
    }
}