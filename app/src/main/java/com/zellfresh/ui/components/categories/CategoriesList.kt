package com.zellfresh.ui.components.categories

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zellfresh.client.apollo.dto.Result
import com.zellfresh.ui.components.imagebutton.ImageButton


@Composable
fun CategoriesList(
    categoriesList: Result<Categories>,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (categoriesList) {
        is Result.Loading -> CircularProgressIndicator()
        is Result.Failure -> Text(text = "Error: ${categoriesList.exception.message}")
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
