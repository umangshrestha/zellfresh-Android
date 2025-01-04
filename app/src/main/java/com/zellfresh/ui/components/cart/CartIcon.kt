package com.zellfresh.ui.components.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.ColorFilter
import com.zellfresh.R

@Composable
fun CartIcon(
    cartCount: Int,
    modifier: Modifier = Modifier,
) {
    BadgedBox(badge = {
        if (cartCount != 0) {
            Badge(
                content = {
                    Text(text = cartCount.toString())
                },
            )
        }
    }) {
        Image(
            painter = painterResource(R.drawable.cart),
            contentDescription = "cart",
            modifier = modifier,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
    }
}


@Preview(showBackground = false)
@Composable
fun CartIconPreview() {

    var count by remember {
        mutableStateOf(0)
    }
    Row(
        modifier = Modifier
            .width(300.dp)
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(
            onClick = { count.dec() },
        ) {
            Text(
                text = "-",
                style = typography.headlineMedium,
            )
        }
        CartIcon(cartCount = 2, modifier = Modifier.size(40.dp))
        OutlinedButton(
            onClick = { count.inc() }
        ) {
            Text(
                text = "+",
                style = typography.headlineMedium,
            )
        }
    }
}