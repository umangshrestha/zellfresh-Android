package  com.zellfresh.ui.components.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zellfresh.ui.components.shimmerLoading

@Composable
fun ProductItemSkeleton(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .width(100.dp)
                    .height(16.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                    .shimmerLoading()
            )

            Spacer(modifier = modifier.height(8.dp))

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
            )

            Spacer(modifier = modifier.height(8.dp))

            Box(
                modifier = modifier
                    .fillMaxWidth(0.6f)
                    .height(20.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
            )

            Spacer(modifier = modifier.height(8.dp))

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
            )

            Spacer(modifier = modifier.height(8.dp))

            Box(
                modifier = modifier
                    .fillMaxWidth(0.3f)
                    .height(20.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
            )

            Spacer(modifier = modifier.height(8.dp))

            Box(
                modifier = modifier
                    .fillMaxWidth(0.4f)
                    .height(16.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
            )

            Spacer(modifier = modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(16.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = modifier
                        .width(60.dp)
                        .height(16.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                    .shimmerLoading()
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductItemSkeletonPreview() {
    ProductItemSkeleton()
}