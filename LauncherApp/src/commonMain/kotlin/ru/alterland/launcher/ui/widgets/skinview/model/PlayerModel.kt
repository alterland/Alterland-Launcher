package ru.alterland.launcher.ui.widgets.skinview.model

import ru.alterland.launcher.domain.model.Skin

data class PlayerModelState(
    val head: BodyPartState = BodyPartState(),
    val body: BodyPartState = BodyPartState(),
    val leftArm: BodyPartState = BodyPartState(),
    val rightArm: BodyPartState = BodyPartState(),
    val leftLeg: BodyPartState = BodyPartState(),
    val rightLeg: BodyPartState = BodyPartState()
)

class PlayerModel(modelType: Skin.ModelType = Skin.ModelType.WIDE) {

    private val armWidth = if (modelType == Skin.ModelType.SLIM) 3f else 4f
    private val armOffsetX = if (modelType == Skin.ModelType.SLIM) 5.5f else 6f

    private val head = createHead()
    private val body = createBody()
    private val rightArm = createArm(isLeft = false)
    private val leftArm = createArm(isLeft = true)
    private val rightLeg = createLeg(isLeft = false)
    private val leftLeg = createLeg(isLeft = true)

    fun getAllFaces(
        state: PlayerModelState,
        globalRotationX: Float,
        globalRotationY: Float,
        showOuterLayer: Boolean = true
    ): List<Face> = buildList {
        val params = GlobalTransformParams(globalRotationX, globalRotationY, showOuterLayer)

        addAll(head.getFaces(state.head, params))
        addAll(body.getFaces(state.body, params))
        addAll(rightArm.getFaces(state.rightArm, params))
        addAll(leftArm.getFaces(state.leftArm, params))
        addAll(rightLeg.getFaces(state.rightLeg, params))
        addAll(leftLeg.getFaces(state.leftLeg, params))
    }

    private fun BodyPart.getFaces(state: BodyPartState, params: GlobalTransformParams) =
        getFaces(state, params.rotationX, params.rotationY, MODEL_CENTER_Y, params.showOuterLayer)

    private fun createHead() = BodyPart(
        innerBox = Box3D(8f, 8f, 8f, uvX = 0, uvY = 0),
        outerBox = Box3D(9f, 9f, 9f, uvX = 32, uvY = 0, texW = 8, texH = 8, texD = 8),
        pivotX = 0f, pivotY = -4f, pivotZ = 0f,
        offsetX = 0f, offsetY = 12f, offsetZ = 0f
    )

    private fun createBody() = BodyPart(
        innerBox = Box3D(8f, 12f, 4f, uvX = 16, uvY = 16),
        outerBox = Box3D(8.5f, 12.5f, 4.5f, uvX = 16, uvY = 32, texW = 8, texH = 12, texD = 4),
        pivotX = 0f, pivotY = 0f, pivotZ = 0f,
        offsetX = 0f, offsetY = 2f, offsetZ = 0f
    )

    private fun createArm(isLeft: Boolean): BodyPart {
        val sign = if (isLeft) 1 else -1
        val innerUvX = if (isLeft) 32 else 40
        val innerUvY = if (isLeft) 48 else 16
        val outerUvX = if (isLeft) 48 else 40
        val outerUvY = if (isLeft) 48 else 32

        return BodyPart(
            innerBox = Box3D(armWidth, 12f, 4f, uvX = innerUvX, uvY = innerUvY),
            outerBox = Box3D(armWidth + 0.5f, 12.5f, 4.5f, uvX = outerUvX, uvY = outerUvY, texW = armWidth.toInt(), texH = 12, texD = 4),
            pivotX = -sign * armWidth / 2f, pivotY = 6f, pivotZ = 0f,
            offsetX = sign * armOffsetX, offsetY = 2f, offsetZ = 0f
        )
    }

    private fun createLeg(isLeft: Boolean): BodyPart {
        val sign = if (isLeft) 1 else -1
        val innerUvX = if (isLeft) 16 else 0
        val innerUvY = if (isLeft) 48 else 16
        val outerUvX = 0
        val outerUvY = if (isLeft) 48 else 32

        return BodyPart(
            innerBox = Box3D(4f, 12f, 4f, uvX = innerUvX, uvY = innerUvY),
            outerBox = Box3D(4.5f, 12.5f, 4.5f, uvX = outerUvX, uvY = outerUvY, texW = 4, texH = 12, texD = 4),
            pivotX = 0f, pivotY = 6f, pivotZ = 0f,
            offsetX = sign * 2f, offsetY = -10f, offsetZ = 0f
        )
    }

    private data class GlobalTransformParams(
        val rotationX: Float,
        val rotationY: Float,
        val showOuterLayer: Boolean
    )

    companion object {
        private const val MODEL_CENTER_Y = 0f
    }
}
