package com.zellfresh.ui.components.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zellfresh.client.ListCategoriesQuery.Category
import com.zellfresh.client.apollo.dto.Result
import com.zellfresh.ui.components.ErrorComponent
import com.zellfresh.ui.components.imagebutton.ImageButton
import com.zellfresh.ui.components.imagebutton.ImageButtonSkeleton


@Composable
fun CategoriesScreen(
    categoriesList: Result<List<Category>>,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Categories",
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            textAlign = TextAlign.Center,
            style = typography.headlineLarge,
        )
        Text(
            text = "Please select categories to continue",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            style = typography.headlineMedium,
            modifier = modifier.fillMaxWidth()
        )
        LazyColumn(
            modifier = modifier
                .fillMaxHeight()
                .padding(top = 36.dp)
        ) {
            when (categoriesList) {
                is Result.Loading ->
                    repeat(4) {
                        item {
                            ImageButtonSkeleton()
                        }
                    }

                is Result.Failure ->
                    item {
                        ErrorComponent(
                            text = categoriesList.exception.message ?: "Error fetching categories",
                            modifier = modifier
                        )
                    }

                is Result.Success -> {
                    items(categoriesList.data) { category ->
                        ImageButton(url = category.imageUrl,
                            contentDescription = category.name,
                            text = category.name,
                            onClick = {
                                onNavigate(category.name)
                            })
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CategoriesScreenLoading() {
    CategoriesScreen(Result.Loading(), {})
}


@Preview(showBackground = true)
@Composable
fun CategoriesScreenError() {
    CategoriesScreen(Result.Failure(Exception("Error")), {})
}

@Preview(showBackground = true)
@Composable
fun CategoriesScreenSuccess() {
    CategoriesScreen(Result.Success(List(4) {
        Category(
            name = it.toString(),
            imageUrl = "",
        )
    }), {})
}