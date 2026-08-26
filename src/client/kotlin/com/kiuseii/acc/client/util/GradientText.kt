package com.kiuseii.acc.client.util

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.TextColor

object GradientText {
    fun build(text: String, startColor: Int, endColor: Int): MutableComponent {
        val result = Component.literal("")

        val startR = (startColor shr 16) and 0xFF
        val startG = (startColor shr 8) and 0xFF
        val startB = startColor and 0xFF

        val endR = (endColor shr 16) and 0xFF
        val endG = (endColor shr 8) and 0xFF
        val endB = endColor and 0xFF

        val length = text.length

        text.forEachIndexed { index, char ->
            val ratio = if (length <= 1) 0.0 else index.toDouble() / (length - 1)

            val r = (startR + (endR - startR) * ratio).toInt()
            val g = (startG + (endG - startG) * ratio).toInt()
            val b = (startB + (endB - startB) * ratio).toInt()

            val rgb = (r shl 16) or (g shl 8) or b
            val color = TextColor.fromRgb(rgb)

            result.append(
                Component.literal(char.toString()).withStyle { it.withColor(color) }
            )
        }

        return result
    }
}