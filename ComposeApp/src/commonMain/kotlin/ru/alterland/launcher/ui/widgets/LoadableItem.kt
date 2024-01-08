package widgets

import androidx.compose.runtime.Composable

@Composable
fun LoadableItem(
    isLoading: Boolean = false,
    placeholder: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    if (isLoading) placeholder() else content()
}