package com.kiuseii.acc.client.album.tooltip

import com.kiuseii.acc.client.album.data.CardAlbumSlot
import com.kiuseii.acc.client.cards.data.CardDataParser
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.resources.ResourceLocation

class CardAlbumClientTooltip(private val slots: List<CardAlbumSlot>) : ClientTooltipComponent {
    companion object {
        private const val ICON_SIZE = 16

        private const val FRAME_WIDTH = 20
        private const val FRAME_HEIGHT = 29
        private const val FRAME_U = 1
        private const val FRAME_V = 2
        private const val FRAME_SHEET_SIZE = 64

        private const val ICON_OFFSET_X = (FRAME_WIDTH - ICON_SIZE) / 2
        private const val ICON_OFFSET_Y = 2

        private const val SLOT_MARGIN = 3
        private const val SLOT_WIDTH = FRAME_WIDTH + SLOT_MARGIN
        private const val SLOT_HEIGHT = FRAME_HEIGHT + SLOT_MARGIN
        private const val COLUMNS = 6
        private const val MAX_ROWS = 2
    }

    private val rowCount: Int = when {
        slots.isEmpty() -> 0
        slots.size <= COLUMNS -> 1
        else -> MAX_ROWS
    }

    override fun getHeight(): Int = rowCount * SLOT_HEIGHT

    override fun getWidth(font: Font): Int = COLUMNS * SLOT_WIDTH

    override fun renderImage(font: Font, x: Int, y: Int, graphics: GuiGraphics) {
        for (slotIndex in 0 until 12) {
            val col = slotIndex % COLUMNS
            val row = slotIndex / COLUMNS
            val slotX = x + col * SLOT_WIDTH
            val slotY = y + row * SLOT_HEIGHT

            val slot = slots.firstOrNull { it.index == slotIndex } ?: continue
            val cardTag = slot.cardTag
            val icon = cardTag.getString("icon")
            val rarity = cardTag.getString("rarity")
            val grade = CardDataParser.extractGrade(cardTag)

            val iconTexture = resolveIconTexture(icon, rarity)
            val frameTexture = resolveFrameTexture(rarity)

            graphics.blit(
                frameTexture,
                slotX, slotY,
                FRAME_U.toFloat(), FRAME_V.toFloat(),
                FRAME_WIDTH, FRAME_HEIGHT,
                FRAME_SHEET_SIZE, FRAME_SHEET_SIZE
            )

            graphics.blit(
                iconTexture,
                slotX + ICON_OFFSET_X, slotY + ICON_OFFSET_Y,
                0f, 0f,
                ICON_SIZE, ICON_SIZE,
                ICON_SIZE, ICON_SIZE
            )

            if (grade > 0) {
                val gradeText = grade.toString()
                graphics.drawString(
                    font,
                    gradeText,
                    slotX + FRAME_WIDTH - font.width(gradeText),
                    slotY + FRAME_HEIGHT - 8,
                    0xFFAA00,
                    true
                )
            }
        }
    }

    private fun resolveIconTexture(icon: String, rarity: String): ResourceLocation {
        val iconName = if (rarity.equals("SHINY", ignoreCase = true)) "${icon}shiny" else icon

        return ResourceLocation.fromNamespaceAndPath("academy", "textures/item/card/icon/$iconName.png")
    }

    private fun resolveFrameTexture(rarity: String): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath("academy", "textures/item/card/frame/${rarity.lowercase()}.png")
}