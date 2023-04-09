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
import com.nishitnagar.splitnish.data.entity.TransactionEntity
import com.nishitnagar.splitnish.enums.Currency
import com.nishitnagar.splitnish.enums.VisibilityState
import com.nishitnagar.splitnish.CreatorActivity
import com.nishitnagar.splitnish.ui.previewprovider.TransactionEntitiesProvider
import com.nishitnagar.splitnish.util.Helper
import java.time.LocalDateTime

@Composable
fun ControlTransactionScreen(
    visibilityState: MutableState<VisibilityState>,
    transactionEntities: State<List<TransactionEntity>>,
    onCreate: (TransactionEntity) -> Unit
) {
    when (visibilityState.value) {
        VisibilityState.VISIBLE -> {
            TransactionScreen(transactionEntities = transactionEntities,
                onCreate = onCreate,
                onDismiss = { visibilityState.value = VisibilityState.HIDDEN })
        }
        VisibilityState.HIDDEN -> {
            /* Do Nothing */
        }
    }
}

@Composable
fun TransactionScreen(
    transactionEntities: State<List<TransactionEntity>>, onCreate: (TransactionEntity) -> Unit, onDismiss: () -> Unit
) {
    val context = LocalContext.current
    Column {
        Row {
            Button(onClick = { context.startActivity(Intent(context, CreatorActivity::class.java)) }) {
                Text("Create Transaction")
            }
            Button(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
        TransactionLazyColumn(transactionEntities = transactionEntities)
    }
}

@Composable
fun TransactionLazyColumn(@PreviewParameter(TransactionEntitiesProvider::class) transactionEntities: State<List<TransactionEntity>>) {
    LazyColumn {
        item {
            HeadingText(text = "Transaction Entities")
        }
        items(transactionEntities.value) { transactionEntity ->
            TransactionRow(transactionEntity)
        }
    }
}

@Composable
fun TransactionRow(item: TransactionEntity) {
    Text(text = Helper.gson.toJson(item), modifier = Modifier.padding(8.dp))
}

@Preview(showBackground = true)
@Composable
fun PreviewTransactionScreen() {
    val transactionEntities = remember {
        mutableStateOf(
            listOf(
                TransactionEntity(dateTime = LocalDateTime.now(), currency = Currency.INR, amount = 500.0, note = ""),
                TransactionEntity(dateTime = LocalDateTime.now(), currency = Currency.INR, amount = 1500.0, note = ""),
                TransactionEntity(dateTime = LocalDateTime.now(), currency = Currency.INR, amount = 2500.0, note = ""),
            )
        )
    }
    TransactionScreen(transactionEntities = transactionEntities, onCreate = {}, onDismiss = {})
}
