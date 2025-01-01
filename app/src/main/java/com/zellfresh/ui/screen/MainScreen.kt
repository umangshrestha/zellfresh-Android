package com.zellfresh.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zellfresh.ui.theme.ThemeToggle
import com.zellfresh.ui.theme.ThemeViewModel
import com.zellfresh.ui.theme.ZellfreshTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zellfresh.R
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.zellfresh.client.http.HttpViewModel
import com.zellfresh.ui.components.categories.CategoriesList
import com.zellfresh.ui.components.categories.CategoriesViewModel
import com.zellfresh.ui.components.notification.NotificationController
import com.zellfresh.ui.components.notification.ObserveAsEvents
import com.zellfresh.ui.components.products.ProductsList
import com.zellfresh.ui.components.products.ProductsViewModel
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel = viewModel(),
    httpViewModel: HttpViewModel = viewModel(),
    categoriesViewModel: CategoriesViewModel = viewModel(),
    productsViewModel: ProductsViewModel = viewModel(),
) {
    val appName = stringResource(id = R.string.app_name)
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val accountDetails by httpViewModel.accountDetails.collectAsState()
    ZellfreshTheme(isDarkTheme = isDarkTheme) {
        ObserveAsEvents(flow = NotificationController.notifications) {
            scope.launch {
                snackBarHostState.currentSnackbarData?.dismiss()
                val result = snackBarHostState.showSnackbar(
                    message = it.message,
                    actionLabel = it.actionLabel,
                    duration = it.duration,
                    withDismissAction = true,
                )
                if (result == SnackbarResult.ActionPerformed) {
                    it.action()
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = appName, color = Color.White, modifier = Modifier.clickable(
                            onClick = {
                                navController.navigate("home")
                            }
                        ))
                    },
                    actions = {
                        ThemeToggle(
                            isDarkTheme = isDarkTheme,
                            onClick = {
                                themeViewModel.setTheme(!isDarkTheme)
                                themeViewModel.setTheme(!isDarkTheme)
                            }
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            },
            modifier = modifier.fillMaxSize(),
        ) { innerPadding ->
            NavHost(
                navController = navController, startDestination = "home",
                modifier = modifier.padding(innerPadding)
            ) {
                composable(route = "login") {
                    LoginScreen(
                        navController = navController,
                        modifier = modifier
                    )
                }
                composable(route = "home") {
                    val categoriesList by categoriesViewModel.categoriesState.collectAsState()
                    Column(modifier = modifier.fillMaxSize()) {
                        Text(
                            text = "Categories",
                            modifier = modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = typography.headlineMedium,
                        )
                        CategoriesList(
                            categoriesList,
                            onNavigate = { navController.navigate(it) },
                            modifier = modifier
                        )
                    }
                }
                composable(
                    route = "/products?category={category}",
                    arguments = listOf(navArgument("category") {
                        type = NavType.StringType
                    })
                ) { backStackEntry ->
                    val productsList by productsViewModel.productsState.collectAsState()
                    LaunchedEffect(Unit) {
                        val category = backStackEntry.arguments?.getString("category")
                        productsViewModel.getProducts(category, reset = true)
                    }
                    ProductsList(
                        productsList,
                        fetchMore = {
                            val category = backStackEntry.arguments?.getString("category")
                            productsViewModel.getProducts(category)
                        },
                        onAddItemToCart = {

                        },
                        modifier = modifier
                    )
                }
                composable(route = "cart") {
                    CartScreen(
                        navController = navController,
                        modifier = modifier
                    )
                }
                composable(route = "orders") {
                    OrdersScreen(
                        navController = navController,
                        modifier = modifier
                    )
                }
            }
        }
    }
}