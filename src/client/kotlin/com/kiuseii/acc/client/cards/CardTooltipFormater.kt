package com.kiuseii.acc.client.cards

import com.kiuseii.acc.client.util.GradientText
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor

object CardTooltipFormater {
    fun formatRarity(rarity: String): Component {
        val resolvedRarity = I18n.get("text.academy.card.rarity." + rarity.lowercase())
        val (startColor, endColor) = CardStat.getGradients(rarity)

        val coloredRarity = GradientText.build(resolvedRarity, startColor, endColor)

        return Component.translatable("text.academy.card.rarity")
            .append(": ")
            .append(coloredRarity)
    }

    fun formatGrade(grade: Int): Component {
        val multiplier = CardStat.getMultiplier(grade)

        return if(grade != 0) {
            Component.translatable("text.academy.card.grade")
                .append(": ")
                .append(Component.literal("$grade").withStyle(ChatFormatting.GOLD))
                .append(Component.literal(" (x${"%.2f".format(multiplier)})").withStyle(ChatFormatting.DARK_GRAY))
        } else {
            Component.translatable("text.academy.card.grade").withStyle(ChatFormatting.GRAY)
                .append(": ???").withStyle(ChatFormatting.GRAY)
        }
    }

    fun formatModifier(card: CardStatValue, grade: Int): Component {
        val value = card.value.toDouble()
        val multiplier = CardStat.getMultiplier(grade)

        val definition = CardStat.get(card.source) ?: return Component.literal("§7${card.source}: §f$value")

        val gradedValue = value * multiplier
        val displayedRawValue = if (Screen.hasShiftDown()) value else gradedValue

        var displayValue = if (definition.percent) {
            "+%.2f%%".format(displayedRawValue * 100)
        } else {
            "+${"%.2f".format(displayedRawValue)}"
        }

        if(Screen.hasShiftDown()) {
            if (definition.percent) {
                val minAdjusted = "%.2f%%".format(card.minInclusive.toDouble() * 100)
                val maxAdjusted = "%.2f%%".format(card.maxInclusive.toDouble() * 100)

                displayValue += " [$minAdjusted-$maxAdjusted]"
            } else {
                val minAdjusted = "%.2f".format(card.minInclusive.toDouble())
                val maxAdjusted = "%.2f".format(card.maxInclusive.toDouble())

                displayValue += " [$minAdjusted-$maxAdjusted]"
            }
        }

        val textColor = TextColor.parseColor(definition.color).result().orElse(TextColor.fromRgb(0xFFFFFF))

        return Component.literal("$displayValue ").withStyle { it.withColor(textColor) }
            .append(Component.translatable(definition.key).withStyle { it.withColor(textColor) })
    }

    fun formatDetail(): Component {
        return if (!Screen.hasShiftDown()) {
            Component.translatable("text.acc.card.display.shift_hint_show")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
        } else {
            Component.translatable("text.acc.card.display.shift_hint_hide")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
        }
    }
}