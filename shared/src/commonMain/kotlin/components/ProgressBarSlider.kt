package components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBarSlider(
    color: Color,
    modifier: Modifier = Modifier
) {
    val path = pathOfSlider()
    val backgroundColor = backgroundColor
    Canvas(
        modifier = modifier
            .size(width = 24.dp, height = 20.dp)
    ) {
        drawPath(
            path = path,
            color = color
        )
        drawPath(
            path = path,
            color = backgroundColor,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}

/**
 * create [Path] of triangle slider with width 24.[dp] and height 20.dp
 */
@Stable
@Composable
private fun pathOfSlider(): Path {
    val path = Path()
    with(LocalDensity.current) {
        with(path) {
            fun Double.toPxFloat(): Float = this.dp.roundToPx().toFloat()

            moveTo(
                x = 16.859.toPxFloat(),
                y = 16.484.toPxFloat()
            )
            cubicTo(
                x1 = 15.111.toPxFloat(),
                y1 = 19.397.toPxFloat(),
                x2 = 10.889.toPxFloat(),
                y2 = 19.397.toPxFloat(),
                x3 = 9.141.toPxFloat(),
                y3 = 16.484.toPxFloat()
            )
            lineTo(
                x = 2.44.toPxFloat(),
                y = 5.315.toPxFloat()
            )
            cubicTo(
                x1 = 0.64.toPxFloat(),
                y1 = 2.316.toPxFloat(),
                x2 = 2.801.toPxFloat(),
                y2 = (-1.5).toPxFloat(),
                x3 = 6.299.toPxFloat(),
                y3 = (-1.5).toPxFloat()
            )
            lineTo(
                x = 19.701.toPxFloat(),
                y = (-1.5).toPxFloat()
            )
            cubicTo(
                x1 = 23.199.toPxFloat(),
                y1 = (-1.5).toPxFloat(),
                x2 = 25.36.toPxFloat(),
                y2 = 2.316.toPxFloat(),
                x3 = 23.56.toPxFloat(),
                y3 = 5.315.toPxFloat()
            )
            lineTo(
                x = 16.859.toPxFloat(),
                y = 16.484.toPxFloat()
            )
            close()
        }
    }
    return path
}
