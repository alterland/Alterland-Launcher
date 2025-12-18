package ru.alterland.launcher.ui.screen.main.skins

import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.repository.SkinRepository
import ru.alterland.launcher.ui.base.BaseViewModel

class SkinsViewModel(
    private val skinRepository: SkinRepository,
    private val payload: SkinsPayload
): BaseViewModel<SkinsContract.State, SkinsContract.Effect, SkinsContract.Action>() {

    override val container = container<SkinsContract.State, SkinsContract.Effect>(SkinsContract.State())

    override fun dispatch(action: SkinsContract.Action) {

    }
}
