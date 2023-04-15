package components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ProgressRangedColors(
    val colors: List<RangedColor>
) {
    data class RangedColor(
        val range: IntRange,
        val color: Color
    )
}

class ProgressRangedColorsBuilder {

    private val colors = mutableListOf<Pair<IntRange, Color>>()

    infix fun Color.setToRange(range: IntRange) {
        colors.add(range to this)
    }

    fun build(): ProgressRangedColors {
        return ProgressRangedColors(
            colors.map {
                ProgressRangedColors.RangedColor(
                    range = it.first,
                    color = it.second
                )
            }
        )
    }
}

@Composable
fun progressRangedColors(
    init: @Composable ProgressRangedColorsBuilder.() -> Unit
): ProgressRangedColors {
    val builder = ProgressRangedColorsBuilder()
    builder.init()
    return builder.build()
}
