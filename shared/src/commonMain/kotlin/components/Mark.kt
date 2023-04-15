package components

import androidx.compose.runtime.Immutable

fun Mark(value: Int): Mark = Mark("$value", value)

data class Mark(
    val label: String,
    val value: Int
)

@Immutable
data class Marks(
    val marks: List<Mark>
)

fun ProgressRangedColors.toMarks(): Marks {
    return Marks(
        marks = colors.map { (range, _) ->
            val value = range.last
            Mark(
                label = "${value + 1}",
                value = value
            )
        }.take((colors.size - 1).coerceAtLeast(0))
    )
}
