package com.zellfresh.ui.components.categories

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zellfresh.client.ListCategoriesQuery.Category
import com.zellfresh.client.apollo.dto.Result
import com.zellfresh.ui.components.imagebutton.ImageButton
import com.zellfresh.ui.components.imagebutton.ImageButtonSkeleton


@Composable
fun CategoriesList(
    categoriesList: Result<List<Category>>,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (categoriesList) {
        is Result.Loading -> LazyColumn(modifier = modifier) {
            repeat(4) {
                item {
                    ImageButtonSkeleton()
                }
            }
        }

        is Result.Failure -> Text(
            text = "Error: ${categoriesList.exception.message}",
            modifier = modifier
        )

        is Result.Success -> {
            LazyColumn(modifier = modifier) {
                items(categoriesList.data) { category ->
                    ImageButton(url = category.imageUrl,
                        contentDescription = category.name,
                        text = category.name,
                        onClick = {
                            onNavigate(category.navigateUrl)
                        })
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CategoriesListLoading() {
    CategoriesList(
        Result.Loading(),
        {}
    )
}


@Preview(showBackground = true)
@Composable
fun CategoriesListError() {
    CategoriesList(
        Result.Failure(Exception("Error")),
        {}
    )
}

@Preview(showBackground = true)
@Composable
fun CategoriesListSuccess() {
    CategoriesList(
        Result.Success(
            List(4) {
                Category(
                    name = it.toString(),
                    imageUrl = "",
                    navigateUrl = "",
                )
            }
        ),
        {}
    )
}