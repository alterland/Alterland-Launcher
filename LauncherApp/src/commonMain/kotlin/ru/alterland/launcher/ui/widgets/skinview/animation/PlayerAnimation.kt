package ru.alterland.launcher.ui.widgets.skinview.animation

import ru.alterland.launcher.ui.widgets.skinview.model.BodyPartState
import ru.alterland.launcher.ui.widgets.skinview.model.PlayerModelState
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

interface PlayerAnimation {
    fun animate(progress: Float): PlayerModelState
}

class IdleAnimation : PlayerAnimation {
    override fun animate(progress: Float): PlayerModelState {
        val t = progress * 2f

        val leftArmZ = cos(t) * 0.03f + (PI.toFloat() * 0.02f)
        val rightArmZ = cos(t + PI.toFloat()) * 0.03f - (PI.toFloat() * 0.02f)

        return PlayerModelState(
            leftArm = BodyPartState(rotationZ = leftArmZ),
            rightArm = BodyPartState(rotationZ = rightArmZ)
        )
    }
}

class WalkingAnimation : PlayerAnimation {
    override fun animate(progress: Float): PlayerModelState {
        val t = progress * 8f

        // Leg swing - 0.5 rad amplitude
        val leftLegX = sin(t) * 0.5f
        val rightLegX = sin(t + PI.toFloat()) * 0.5f

        // Arm swing - opposite to legs
        val leftArmX = sin(t + PI.toFloat()) * 0.5f
        val rightArmX = sin(t) * 0.5f

        // Arm sway
        val leftArmZ = cos(t) * 0.03f + (PI.toFloat() * 0.02f)
        val rightArmZ = cos(t + PI.toFloat()) * 0.03f - (PI.toFloat() * 0.02f)

        return PlayerModelState(
            leftArm = BodyPartState(rotationX = leftArmX, rotationZ = leftArmZ),
            rightArm = BodyPartState(rotationX = rightArmX, rotationZ = rightArmZ),
            leftLeg = BodyPartState(rotationX = leftLegX),
            rightLeg = BodyPartState(rotationX = rightLegX)
        )
    }
}

class RunningAnimation : PlayerAnimation {
    override fun animate(progress: Float): PlayerModelState {
        val t = progress * 15f + PI.toFloat() * 0.5f

        // Leg swing - 1.3 rad amplitude
        val leftLegX = cos(t + PI.toFloat()) * 1.3f
        val rightLegX = cos(t) * 1.3f

        // Arm swing - 1.5 rad amplitude
        val leftArmX = cos(t) * 1.5f
        val rightArmX = cos(t + PI.toFloat()) * 1.5f

        // Arm sway
        val leftArmZ = cos(t) * 0.1f + (PI.toFloat() * 0.1f)
        val rightArmZ = cos(t + PI.toFloat()) * 0.1f - (PI.toFloat() * 0.1f)

        return PlayerModelState(
            leftArm = BodyPartState(rotationX = leftArmX, rotationZ = leftArmZ),
            rightArm = BodyPartState(rotationX = rightArmX, rotationZ = rightArmZ),
            leftLeg = BodyPartState(rotationX = leftLegX),
            rightLeg = BodyPartState(rotationX = rightLegX)
        )
    }
}

enum class AnimationType {
    IDLE,
    WALKING,
    RUNNING
}

fun getAnimation(type: AnimationType): PlayerAnimation {
    return when (type) {
        AnimationType.IDLE -> IdleAnimation()
        AnimationType.WALKING -> WalkingAnimation()
        AnimationType.RUNNING -> RunningAnimation()
    }
}
