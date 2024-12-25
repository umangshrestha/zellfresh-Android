package com.zellfresh.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zellfresh.ui.theme.ThemeToggle
import com.zellfresh.ui.theme.ThemeViewModel
import com.zellfresh.ui.theme.ZellfreshTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zellfresh.R


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(
    viewModel: ThemeViewModel = viewModel()
) {
    val appName = stringResource(id = R.string.app_name)
    val isDarkTheme by viewModel.isDarkTheme.collectAsState(
        initial = isSystemInDarkTheme()
    )
    val navController = rememberNavController()
    ZellfreshTheme(
        isDarkTheme = isDarkTheme
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = appName, color = Color.White, modifier = Modifier.clickable(
                            onClick = {
                                navController.navigate("login")
                            }
                        ))

                    },
                    navigationIcon = {
                        if (navController.previousBackStackEntry != null) {
                            IconButton(onClick = {
                                navController.navigateUp()
                            }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "backIcon")
                            }
                        }
                    },
                    actions = {
                        ThemeToggle(
                            isDarkTheme = isDarkTheme,
                            onClick = {
                                viewModel.setTheme(!isDarkTheme)
                            }
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            NavHost(
                navController = navController, startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = "login") {
                    LoginScreen(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                composable(route = "home") {
                    HomeScreen(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                composable(route = "products") {
                    ProductsScreen(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                composable(route = "cart") {
                    CartScreen(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                composable(route = "orders") {
                    OrdersScreen(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainScreen()
}