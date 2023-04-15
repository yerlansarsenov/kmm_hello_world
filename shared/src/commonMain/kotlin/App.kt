import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.MAX_VALUE
import components.MIN_VALUE
import components.Mark
import components.Marks
import components.RangedProgressBar
import components.backgroundColor
import components.labelColor
import components.progressRangedColors

private const val v1 = 30
private const val v2 = 70

@Composable
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Space(4.dp)
            Text(
                text = "Rating of pets",
                color = labelColor,
                fontSize = 45.sp
            )
            Space(16.dp)
            listOf(
                "Dogs" to 90,
                "Spider" to 23,
                "Cats" to 86,
                "Camel" to 64,
                "Helicopter" to 43,
                "Fish" to 78
            ).forEach { (label, value) ->
                RatingView(
                    label = label,
                    value = value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun RatingView(
    label: String,
    value: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = labelColor,
            fontSize = 24.sp
        )
        Space(8.dp)
        RangedProgressBar(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            colors = progressRangedColors {
                Color.Red setToRange MIN_VALUE..v1
                Color.Yellow setToRange v1..v2
                Color.Green setToRange v2..MAX_VALUE
            },
            marks = remember {
                Marks(listOf(Mark(v1), Mark(v2)))
            },
            durationMillis = 2500
        )
    }
}

@Composable
private fun ColumnScope.Space(size: Dp) {
    Spacer(modifier = Modifier.height(size))
}

expect fun getPlatformName(): String