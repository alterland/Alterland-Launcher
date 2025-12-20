package ru.alterland.launcher.ui.screen.main.skins

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.alterland.launcher.ui.widgets.skinview.SkinView
import ru.alterland.launcher.ui.widgets.skinview.rememberSkinViewState

@Composable
fun Skins(
    state: SkinsContract.State,
    onAction: (SkinsContract.Action) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        state.selectedSkin?.let { selectedSkin ->
            item {
                SkinView(
                    modifier = Modifier.size(150.dp, 300.dp),
                    state = rememberSkinViewState(
                        skin = selectedSkin,
                        initialAnimation = state.selectedAnimation
                    )
                )
            }
        }
        items(state.skins) { skin ->
            SkinView(
                modifier = Modifier.size(100.dp, 200.dp),
                state = rememberSkinViewState(skin = skin)
            )
        }
    }
}
