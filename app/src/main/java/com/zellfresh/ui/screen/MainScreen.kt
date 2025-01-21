package com.zellfresh.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import com.zellfresh.ui.theme.ZellfreshTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zellfresh.R
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.zellfresh.client.google.GoogleLoginButton
import com.zellfresh.client.http.AccountViewModel
import com.zellfresh.ui.components.cart.CartIcon
import com.zellfresh.ui.components.cart.CartScreen
import com.zellfresh.ui.components.cart.CartsViewModel
import com.zellfresh.ui.components.categories.CategoriesScreen
import com.zellfresh.ui.components.categories.CategoriesViewModel
import com.zellfresh.ui.components.notification.NotificationController
import com.zellfresh.ui.components.notification.ObserveAsEvents
import com.zellfresh.ui.components.products.ProductsScreen
import com.zellfresh.ui.components.products.ProductsViewModel
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(
    modifier: Modifier = Modifier,
    categoriesViewModel: CategoriesViewModel = viewModel(),
    productsViewModel: ProductsViewModel = viewModel(),
    cartsViewModel: CartsViewModel = viewModel(),
    accountViewModel: AccountViewModel = viewModel(),
) {
    val appName = stringResource(id = R.string.app_name)
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val accountDetails by accountViewModel.accountDetails.collectAsState()
    val cartList by cartsViewModel.carts.collectAsState()


    ZellfreshTheme {
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

        ObserveAsEvents(
            flow = accountViewModel.accountDetails
        ) {
            scope.launch {
                cartsViewModel.getCarts()
            }
        }

        Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }, topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = appName,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.clickable(onClick = {
                            navController.navigate("home")
                        })
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
            )
        }, modifier = modifier.fillMaxSize(), bottomBar = {
            NavigationBar(
                modifier = modifier
            ) {
                val cartCount = cartsViewModel.cartCount.collectAsState()
                NavigationBarItem(
                    selected = navController.currentDestination?.route == "orders",
                    onClick = { navController.navigate("orders") },
                    icon = {
                        Image(
                            painter = painterResource(R.drawable.shoppingbag),
                            contentDescription = "orders",
                            modifier = modifier.size(32.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )
                    },
                )
                NavigationBarItem(
                    selected = navController.currentDestination?.route == "profile",
                    onClick = { navController.navigate("profile") },
                    icon = {
                        Image(
                            painter = painterResource(R.drawable.account),
                            contentDescription = "profile",
                            modifier = modifier.size(32.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )
                    },
                )
                NavigationBarItem(
                    selected = navController.currentDestination?.route == "cart",
                    onClick = { navController.navigate("cart") },
                    icon = {
                        CartIcon(
                            cartCount = cartCount.value, modifier = modifier.size(32.dp)
                        )
                    },
                )
            }
        }) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = modifier.padding(innerPadding)
            ) {
                composable(route = "login") {
                    Text("Login to google")
                    GoogleLoginButton(onSuccess = {
                        val credential = it.credential
                        Log.d("GoogleLogin", credential.toString())
                    }, onFailure = {
                        Log.e("GoogleLogin", it.type)
                        Log.e("GoogleLogin", it.errorMessage.toString())
                    })
                }
                composable(route = "home") {
                    val categoriesList by categoriesViewModel.categoriesState.collectAsState()
                    CategoriesScreen(
                        categoriesList = categoriesList,
                        onNavigate = { category: String -> navController.navigate("products?category=${category}") },
                        modifier = modifier
                    )
                }
                composable(
                    route = "products?category={category}",
                    arguments = listOf(navArgument("category") {
                        type = NavType.StringType
                    })
                ) { backStackEntry ->
                    val productsList by productsViewModel.productsState.collectAsState()
                    LaunchedEffect(Unit) {
                        val category = backStackEntry.arguments?.getString("category")
                        productsViewModel.getProducts(category, reset = true)
                    }
                    ProductsScreen(productsList = productsList, fetchMore = {
                        val category = backStackEntry.arguments?.getString("category")
                        productsViewModel.getProducts(category)
                    }, onAddItemToCart = {
                        cartsViewModel.addItemToCart(it, 1)
                    }, modifier = modifier
                    )
                }
                composable(route = "profile") {
                    ProfileScreen(
                        accountDetails, modifier = modifier
                    )
                }
                composable(route = "cart") {
                    val checkoutDetails by cartsViewModel.checkoutDetails.collectAsState()
                    CartScreen(
                        cartList = cartList,
                        checkoutDetails = checkoutDetails,
                        onCheckout = { navController.navigate("checkout") },
                        onAddItemToCart = { productId: String, quantity: Int ->
                            cartsViewModel.addItemToCart(productId, quantity)
                        },
                        onClearCart = {
                            cartsViewModel.clearCart()
                        },
                        modifier = modifier
                    )
                }
                composable(route = "orders") {
                    OrdersScreen(
                        navController = navController, modifier = modifier
                    )
                }
            }
        }
    }
}