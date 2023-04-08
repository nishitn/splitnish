package com.nishitnagar.splitnish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.nishitnagar.splitnish.ui.theme.SplitnishTheme
import com.nishitnagar.splitnish.util.Helper
import com.nishitnagar.splitnish.viewmodel.TransactionViewModel
import com.nishitnagar.splitnish.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transactionViewModel: TransactionViewModel by viewModels()
        val userViewModel: UserViewModel by viewModels()
        Helper.activeUserEntity = userViewModel.getActiveUserEntity()

        setContent {
            SplitnishTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                }
            }
        }
    }
}