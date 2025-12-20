package ru.alterland.launcher.ui.widgets.skinview.model

data class Box3D(
    val width: Float,
    val height: Float,
    val depth: Float,
    val uvX: Int,
    val uvY: Int,
    val textureWidth: Int = 64,
    val textureHeight: Int = 64,
    // Texture dimensions (in pixels) - defaults to geometry size
    // Used for outer layers where geometry is larger but texture size is same as inner layer
    val texW: Int = width.toInt(),
    val texH: Int = height.toInt(),
    val texD: Int = depth.toInt()
) {
    private val halfW = width / 2f
    private val halfH = height / 2f
    private val halfD = depth / 2f

    private val v0 = Vertex3D(-halfW, -halfH, halfD)  // front bottom left
    private val v1 = Vertex3D(halfW, -halfH, halfD)   // front bottom right
    private val v2 = Vertex3D(halfW, halfH, halfD)    // front top right
    private val v3 = Vertex3D(-halfW, halfH, halfD)   // front top left
    private val v4 = Vertex3D(-halfW, -halfH, -halfD) // back bottom left
    private val v5 = Vertex3D(halfW, -halfH, -halfD)  // back bottom right
    private val v6 = Vertex3D(halfW, halfH, -halfD)   // back top right
    private val v7 = Vertex3D(-halfW, halfH, -halfD)  // back top left

    private fun uv(u: Int, v: Int): UV {
        return UV(u.toFloat() / textureWidth, v.toFloat() / textureHeight)
    }

    val faces: List<Face> by lazy {
        val w = texW
        val h = texH
        val d = texD

        listOf(
            // Front face (z+)
            Face(
                vertices = listOf(v0, v1, v2, v3),
                uvs = listOf(
                    uv(uvX + d, uvY + d + h),
                    uv(uvX + d + w, uvY + d + h),
                    uv(uvX + d + w, uvY + d),
                    uv(uvX + d, uvY + d)
                )
            ),
            // Back face (z-)
            Face(
                vertices = listOf(v5, v4, v7, v6),
                uvs = listOf(
                    uv(uvX + d + w + d, uvY + d + h),
                    uv(uvX + d + w + d + w, uvY + d + h),
                    uv(uvX + d + w + d + w, uvY + d),
                    uv(uvX + d + w + d, uvY + d)
                )
            ),
            // Top face (y+)
            Face(
                vertices = listOf(v3, v2, v6, v7),
                uvs = listOf(
                    uv(uvX + d, uvY + d),
                    uv(uvX + d + w, uvY + d),
                    uv(uvX + d + w, uvY),
                    uv(uvX + d, uvY)
                )
            ),
            // Bottom face (y-)
            Face(
                vertices = listOf(v4, v5, v1, v0),
                uvs = listOf(
                    uv(uvX + d + w, uvY + d),
                    uv(uvX + d + w + w, uvY + d),
                    uv(uvX + d + w + w, uvY),
                    uv(uvX + d + w, uvY)
                )
            ),
            // Right face (x+)
            Face(
                vertices = listOf(v1, v5, v6, v2),
                uvs = listOf(
                    uv(uvX + d + w, uvY + d + h),
                    uv(uvX + d + w + d, uvY + d + h),
                    uv(uvX + d + w + d, uvY + d),
                    uv(uvX + d + w, uvY + d)
                )
            ),
            // Left face (x-)
            Face(
                vertices = listOf(v4, v0, v3, v7),
                uvs = listOf(
                    uv(uvX, uvY + d + h),
                    uv(uvX + d, uvY + d + h),
                    uv(uvX + d, uvY + d),
                    uv(uvX, uvY + d)
                )
            )
        )
    }

    fun getFaces(
        rotationX: Float = 0f,
        rotationY: Float = 0f,
        rotationZ: Float = 0f,
        pivotX: Float = 0f,
        pivotY: Float = 0f,
        pivotZ: Float = 0f,
        translateX: Float = 0f,
        translateY: Float = 0f,
        translateZ: Float = 0f
    ): List<Face> {
        return faces.map { face ->
            face.transform(
                rotationX = rotationX,
                rotationY = rotationY,
                rotationZ = rotationZ,
                pivotX = pivotX,
                pivotY = pivotY,
                pivotZ = pivotZ,
                translateX = translateX,
                translateY = translateY,
                translateZ = translateZ
            )
        }
    }
}
