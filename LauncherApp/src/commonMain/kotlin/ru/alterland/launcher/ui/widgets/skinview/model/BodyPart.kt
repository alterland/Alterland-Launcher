package ru.alterland.launcher.ui.widgets.skinview.model

data class BodyPartState(
    val rotationX: Float = 0f,
    val rotationY: Float = 0f,
    val rotationZ: Float = 0f
)

class BodyPart(
    private val innerBox: Box3D,
    private val outerBox: Box3D?,
    private val pivotX: Float,
    private val pivotY: Float,
    private val pivotZ: Float,
    private val offsetX: Float = 0f,
    private val offsetY: Float = 0f,
    private val offsetZ: Float = 0f
) {
    fun getFaces(
        state: BodyPartState,
        globalRotationX: Float,
        globalRotationY: Float,
        globalPivotY: Float = 0f,
        showOuterLayer: Boolean = true
    ): List<Face> = buildList {
        addAll(transformBox(innerBox, state, globalRotationX, globalRotationY, globalPivotY, isOuter = false))

        if (showOuterLayer && outerBox != null) {
            addAll(transformBox(outerBox, state, globalRotationX, globalRotationY, globalPivotY, isOuter = true))
        }
    }

    private fun transformBox(
        box: Box3D,
        state: BodyPartState,
        globalRotationX: Float,
        globalRotationY: Float,
        globalPivotY: Float,
        isOuter: Boolean
    ): List<Face> = box.getFaces(
        rotationX = state.rotationX,
        rotationY = state.rotationY,
        rotationZ = state.rotationZ,
        pivotX = pivotX,
        pivotY = pivotY,
        pivotZ = pivotZ,
        translateX = offsetX,
        translateY = offsetY,
        translateZ = offsetZ
    ).map { face ->
        face.transform(
            rotationX = globalRotationX,
            rotationY = globalRotationY,
            pivotY = globalPivotY
        ).copy(isOuterLayer = isOuter)
    }
}
