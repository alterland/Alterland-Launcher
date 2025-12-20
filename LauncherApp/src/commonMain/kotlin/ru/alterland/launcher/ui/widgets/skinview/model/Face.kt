package ru.alterland.launcher.ui.widgets.skinview.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

data class UV(
    val u: Float,
    val v: Float
)

data class Face(
    val vertices: List<Vertex3D>,
    val uvs: List<UV>,
    val isOuterLayer: Boolean = false
) {
    fun transform(
        rotationX: Float = 0f,
        rotationY: Float = 0f,
        rotationZ: Float = 0f,
        pivotX: Float = 0f,
        pivotY: Float = 0f,
        pivotZ: Float = 0f,
        translateX: Float = 0f,
        translateY: Float = 0f,
        translateZ: Float = 0f
    ): Face {
        val transformedVertices = vertices.map { vertex ->
            vertex
                .rotateX(rotationX, pivotY, pivotZ)
                .rotateY(rotationY, pivotX, pivotZ)
                .rotateZ(rotationZ, pivotX, pivotY)
                .translate(translateX, translateY, translateZ)
        }
        return copy(vertices = transformedVertices)
    }

    fun normal(): Vertex3D {
        if (vertices.size < 3) return Vertex3D(0f, 0f, 1f)
        val v1 = vertices[1] - vertices[0]
        val v2 = vertices[2] - vertices[0]
        return v1.cross(v2).normalize()
    }

    fun averageZ(): Float = vertices.map { it.z }.average().toFloat()

    fun getTextureRect(textureWidth: Int, textureHeight: Int): Rect {
        val minU = uvs.minOf { it.u }
        val maxU = uvs.maxOf { it.u }
        val minV = uvs.minOf { it.v }
        val maxV = uvs.maxOf { it.v }

        return Rect(
            left = minU * textureWidth,
            top = minV * textureHeight,
            right = maxU * textureWidth,
            bottom = maxV * textureHeight
        )
    }
}

data class ProjectedFace(
    val points: List<Offset>,
    val averageZ: Float,
    val textureRect: Rect,
    val isOuterLayer: Boolean = false
)
