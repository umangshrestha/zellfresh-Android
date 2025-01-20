package com.zellfresh

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.zellfresh.client.NetworkUtils
import com.zellfresh.ui.screen.MainScreen

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        closeAppIfNoInternet()
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }

    private fun closeAppIfNoInternet() {
        if (!NetworkUtils.isInternetAvailable(this)) {
            AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setPositiveButton("Close") { _, _ ->
                    finish()
                }
                .setCancelable(false)
                .show()
        }
    }
}
