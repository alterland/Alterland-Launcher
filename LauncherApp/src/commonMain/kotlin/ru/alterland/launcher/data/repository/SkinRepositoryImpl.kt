package ru.alterland.launcher.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.domain.repository.SkinRepository


class SkinRepositoryImpl(
    private val dispatcherIo: CoroutineDispatcher
): SkinRepository {

    private var skins = listOf<Skin>(
//        Skin(name = "Steve", imageUrl = "skin1.png"),
//        Skin(name = "Alex", imageUrl = "skin1.png"),
//        Skin(name = "Zuri", imageUrl = "skin1.png"),
//        Skin(name = "Sunny", imageUrl = "skin1.png"),
//        Skin(name = "Noor", imageUrl = "skin1.png"),
    )
}