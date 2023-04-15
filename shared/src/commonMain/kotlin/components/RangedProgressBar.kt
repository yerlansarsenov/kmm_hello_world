package components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

const val MIN_VALUE = 0
const val MAX_VALUE = 100

const val StartFraction = 0F
const val MaxFraction = 1F

@Composable
fun RangedProgressBar(
    value: Int,
    colors: ProgressRangedColors,
    modifier: Modifier = Modifier,
    isMarkerLeading: Boolean = false,
    maxValue: Int = MAX_VALUE,
    maxMark: Mark = Mark(maxValue),
    marks: Marks = colors.toMarks(),
    durationMillis: Int = 1000
) {
    val totalHeight = 53.dp
    val height = 25.dp
    BoxWithConstraints(
        modifier = modifier
            .height(totalHeight)
            .background(backgroundColor)
    ) {
        val safeMaxValue = remember(maxValue) {
            maxValue.coerceAtLeast(1)
        }
        val endFraction by animateRangedProgressEndFraction(
            value = value,
            minValue = MIN_VALUE,
            maxValue = maxValue,
            durationMillis = durationMillis
        )
        RangedProgressBarImpl(
            modifier = Modifier
                .padding(top = 7.dp)
                .padding(horizontal = 16.dp)
                .height(height),
            endFraction = endFraction,
            maxValue = safeMaxValue,
            strokeWidth = height,
            colors = colors,
            backgroundColor = progressBackgroundColor
        )
        MarkView(
            modifier = Modifier
                .fillMaxHeight()
                .offset {
                    IntOffset(x = 16.dp.roundToPx(), y = 0)
                },
            isMarkerLeading = false,
            mark = Mark(0)
        )
        MarkView(
            modifier = Modifier
                .fillMaxHeight()
                .offset {
                    IntOffset(x = (maxWidth - 16.dp).roundToPx(), y = 0)
                },
            isMarkerLeading = true,
            mark = maxMark
        )
        marks.marks.forEach { mark ->
            val offset = (maxWidth - 32.dp) * ((mark.value * MaxFraction) / safeMaxValue) + 16.dp
            MarkView(
                modifier = Modifier
                    .fillMaxHeight()
                    .offset {
                        IntOffset(x = offset.roundToPx(), y = 0)
                    },
                mark = mark,
                isMarkerLeading = isMarkerLeading,
                dividerColor = backgroundColor
            )
        }
        val sliderWidth = 24.dp
        Slider(
            modifier = Modifier
                .offset {
                    IntOffset(x = (16.dp - sliderWidth / 2).roundToPx(), y = 0)
                }
                .offset {
                    IntOffset(
                        x = ((maxWidth - 32.dp) * (StartFraction + endFraction)).roundToPx(),
                        y = 0
                    )
                },
            width = sliderWidth,
            maxValue = maxValue,
            endFraction = endFraction,
            colors = colors
        )
    }
}

@Composable
private fun animateRangedProgressEndFraction(
    value: Int,
    minValue: Int,
    maxValue: Int,
    durationMillis: Int
): State<Float> {
    var progress by remember { mutableStateOf(minValue) }
    val transition = updateTransition(
        targetState = progress,
        label = "progress"
    )
    LaunchedEffect(value) {
        progress = value.coerceIn(minValue..maxValue)
    }
    return transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = durationMillis,
                easing = FastOutSlowInEasing
            )
        },
        targetValueByState = { v ->
            (v * MaxFraction) / maxValue
        },
        label = "FractionAnimation"
    )
}

@Composable
private fun MarkView(
    mark: Mark,
    isMarkerLeading: Boolean,
    modifier: Modifier = Modifier,
    dividerColor: Color = Color.Transparent
) {
    Layout(
        modifier = modifier,
        content = {
            // measurables[0]
            Spacer(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(dividerColor)
            )
            // measurables[1]
            Text(
                text = mark.label,
                color = labelColor
            )
        }
    ) { measurables, constraints ->
        layout(constraints.maxWidth, constraints.maxHeight) {
            val dividerPlaceable = measurables[0].measure(
                constraints.copy(
                    maxHeight = constraints.maxHeight,
                    minHeight = 0,
                    minWidth = 0
                )
            )
            dividerPlaceable.place(
                x = 0,
                y = 0
            )

            val markerPlaceable = measurables[1].measure(
                constraints.copy(
                    maxHeight = constraints.maxHeight,
                    minHeight = 0,
                    minWidth = 0
                )
            )
            markerPlaceable.place(
                x = if (isMarkerLeading) {
                    -markerPlaceable.measuredWidth
                } else {
                    dividerPlaceable.measuredWidth
                },
                y = dividerPlaceable.measuredHeight - markerPlaceable.measuredHeight
            )
        }
    }
}

@Composable
private fun Slider(
    width: Dp,
    maxValue: Int,
    endFraction: Float,
    colors: ProgressRangedColors,
    modifier: Modifier = Modifier
) {
    val progress = (endFraction * maxValue).roundToInt()
    val color by animateColorAsState(
        targetValue = colors.colors.first { (range, _) ->
            progress in range
        }.color
    )
    ProgressBarSlider(
        modifier = modifier.width(width),
        color = color
    )
}

@Composable
private fun RangedProgressBarImpl(
    endFraction: Float,
    maxValue: Int,
    strokeWidth: Dp,
    colors: ProgressRangedColors,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    val fractionsToBrush = remember(colors) {
        colors.colors.map { (range, brush) ->
            val startValue = (range.first * MaxFraction) / maxValue
            val endValue = (range.last * MaxFraction) / maxValue
            startValue..endValue to brush
        }
    }
    Canvas(
        modifier = modifier
            .clip(CircleShape)
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        val strokeWidthPx = strokeWidth.toPx()

        drawLinearIndicator(
            startFraction = StartFraction,
            endFraction = MaxFraction,
            color = backgroundColor,
            strokeWidth = strokeWidthPx
        )

        fractionsToBrush.forEach { (range, color) ->

            drawLinearIndicator(
                startFraction = (StartFraction + range.start).coerceAtMost(
                    maximumValue = StartFraction + endFraction
                ),
                endFraction = (StartFraction + range.endInclusive).coerceAtMost(
                    maximumValue = StartFraction + endFraction
                ),
                color = color,
                strokeWidth = strokeWidthPx
            )
        }
    }
}

private fun DrawScope.drawLinearIndicator(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    strokeWidth: Float
) {
    val width = size.width
    val height = size.height
    // Start drawing from the vertical center of the stroke
    val yOffset = height / 2

    val barStart = startFraction * width
    val barEnd = endFraction * width

    // Progress line
    drawLine(color, Offset(barStart, yOffset), Offset(barEnd, yOffset), strokeWidth)
}
