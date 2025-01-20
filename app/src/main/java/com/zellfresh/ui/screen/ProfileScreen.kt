package com.zellfresh.ui.screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zellfresh.client.http.dto.AccountDetails

@Composable
fun ProfileScreen(
    accountDetails: AccountDetails?, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Hello ${accountDetails?.name ?: "Guest" }",
            modifier = modifier
        )
    }

}