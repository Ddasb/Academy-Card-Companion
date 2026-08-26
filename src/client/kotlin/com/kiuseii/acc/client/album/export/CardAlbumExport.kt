package com.kiuseii.acc.client.album.export

import com.kiuseii.acc.Acc
import com.kiuseii.acc.client.album.data.CardAlbumSlot
import com.kiuseii.acc.client.cards.data.CardDataParser
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.pipeline.TextureTarget
import com.mojang.blaze3d.platform.NativeImage
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.ByteBufferBuilder
import com.mojang.blaze3d.vertex.VertexSorting
import com.mojang.serialization.Codec
import net.minecraft.client.Minecraft
import net.minecraft.client.Screenshot
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import org.joml.Matrix4f
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.image.BufferedImage
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

object CardAlbumExport {
    private val CONTAINER_COMPONENT_ID = ResourceLocation.fromNamespaceAndPath("academy", "card_album_container")
    private val BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(Acc.MOD_ID, "textures/gui/card_album.png")

    private const val EXPORT_SCALE = 4

    private const val BG_WIDTH = 256
    private const val BG_HEIGHT = 167

    private val COLS_X = floatArrayOf(15.5f, 52.5f, 89.5f, 137.5f, 174.5f, 211.5f)
    private val ROW_Y = floatArrayOf(28f, 99f)

    private const val FRAME_WIDTH = 20
    private const val FRAME_HEIGHT = 29
    private const val FRAME_U = 1
    private const val FRAME_V = 2
    private const val FRAME_SHEET_SIZE = 64

    private const val ICON_SIZE = 16
    private const val ICON_OFFSET_Y_NATIVE = 2.5f

    private const val CARD_SCALE = 1.45f

    private val DEST_FRAME_WIDTH = (FRAME_WIDTH * CARD_SCALE).toInt()
    private val DEST_FRAME_HEIGHT = (FRAME_HEIGHT * CARD_SCALE).toInt()
    private val DEST_ICON_SIZE = (ICON_SIZE * CARD_SCALE).toInt()

    fun findContainerTag(stack: ItemStack): CompoundTag? {
        for (typedComponent in stack.components) {
            val type = typedComponent.type()
            val id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type) ?: continue
            if (id != CONTAINER_COMPONENT_ID) continue

            @Suppress("UNCHECKED_CAST")
            val codec = type.codec() as? Codec<Any> ?: return null

            val result = codec.encodeStart(NbtOps.INSTANCE, typedComponent.value())
            return result.result().orElse(null) as? CompoundTag
        }

        return null
    }

    fun exportImage(minecraft: Minecraft, slots: List<CardAlbumSlot>) {
        val width = BG_WIDTH * EXPORT_SCALE
        val height = BG_HEIGHT * EXPORT_SCALE

        val target: RenderTarget = TextureTarget(width, height, true, Minecraft.ON_OSX)
        target.setClearColor(0f, 0f, 0f, 0f)
        target.clear(Minecraft.ON_OSX)
        target.bindWrite(true)

        RenderSystem.setProjectionMatrix(
            Matrix4f().setOrtho(0f, width.toFloat(), height.toFloat(), 0f, 1000f, 21000f),
            VertexSorting.ORTHOGRAPHIC_Z
        )

        val bufferSource = MultiBufferSource.immediate(ByteBufferBuilder(256))
        val graphics = GuiGraphics(minecraft, bufferSource)
        graphics.pose().pushPose()
        graphics.pose().translate(0.0, 0.0, -11000.0)
        graphics.pose().scale(EXPORT_SCALE.toFloat(), EXPORT_SCALE.toFloat(), 1f)

        graphics.blit(BACKGROUND_TEXTURE, 0, 0, 0f, 0f, BG_WIDTH, BG_HEIGHT, BG_WIDTH, BG_HEIGHT)

        for (slotIndex in 0 until 12) {
            val col = slotIndex % COLS_X.size
            val row = slotIndex / COLS_X.size

            val frameX = COLS_X[col].toInt()
            val frameY = ROW_Y[row].toInt()

            val slot = slots.firstOrNull { it.index == slotIndex } ?: continue
            val cardTag = slot.cardTag
            val icon = cardTag.getString("icon")
            val rarity = cardTag.getString("rarity")
            val grade = CardDataParser.extractGrade(cardTag)

            val iconOffsetX = (DEST_FRAME_WIDTH - DEST_ICON_SIZE) / 2
            val iconOffsetY = (ICON_OFFSET_Y_NATIVE * DEST_FRAME_HEIGHT / FRAME_HEIGHT).toInt()

            graphics.blit(
                resolveFrameTexture(rarity),
                frameX, frameY,
                DEST_FRAME_WIDTH, DEST_FRAME_HEIGHT,
                FRAME_U.toFloat(), FRAME_V.toFloat(),
                FRAME_WIDTH, FRAME_HEIGHT,
                FRAME_SHEET_SIZE, FRAME_SHEET_SIZE
            )

            graphics.blit(
                resolveIconTexture(icon, rarity),
                frameX + iconOffsetX, frameY + iconOffsetY,
                DEST_ICON_SIZE, DEST_ICON_SIZE,
                0f, 0f,
                ICON_SIZE, ICON_SIZE,
                ICON_SIZE, ICON_SIZE
            )

            if (grade > 0) {
                val gradeText = grade.toString()
                graphics.drawString(
                    minecraft.font,
                    gradeText,
                    frameX + FRAME_WIDTH - minecraft.font.width(gradeText),
                    frameY + 1,
                    0xFFAA00,
                    true
                )
            }
        }

        graphics.pose().popPose()
        bufferSource.endBatch()
        target.unbindWrite()

        val nativeImage = Screenshot.takeScreenshot(target)

        val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Date())
        val fileName = "card_album_$timestamp.png"

        Screenshot.grab(minecraft.gameDirectory, fileName, target) { message ->
            minecraft.player?.displayClientMessage(message, false)
        }

        try {
            copyToClipboard(nativeImageToBufferedImage(nativeImage))
        } catch (e: Exception) {
            minecraft.player?.displayClientMessage(
                Component.translatable("acc.card_album.export_clipboard_failed"),
                false
            )
        }

        target.destroyBuffers()
    }

    private fun resolveIconTexture(icon: String, rarity: String): ResourceLocation {
        val iconName = if (rarity.equals("SHINY", ignoreCase = true)) "${icon}shiny" else icon
        return ResourceLocation.fromNamespaceAndPath("academy", "textures/item/card/icon/$iconName.png")
    }

    private fun resolveFrameTexture(rarity: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath("academy", "textures/item/card/frame/${rarity.lowercase()}.png")
    }

    private fun nativeImageToBufferedImage(image: NativeImage): BufferedImage {
        val width = image.width
        val height = image.height
        val buffered = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val abgr = image.getPixelRGBA(x, y)
                val a = (abgr ushr 24) and 0xFF
                val b = (abgr ushr 16) and 0xFF
                val g = (abgr ushr 8) and 0xFF
                val r = abgr and 0xFF
                buffered.setRGB(x, y, (a shl 24) or (r shl 16) or (g shl 8) or b)
            }
        }
        return buffered
    }

    private fun copyToClipboard(image: BufferedImage) {
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(ImageTransferable(image), null)
    }

    private class ImageTransferable(private val image: BufferedImage) : Transferable {
        override fun getTransferDataFlavors(): Array<DataFlavor> = arrayOf(DataFlavor.imageFlavor)
        override fun isDataFlavorSupported(flavor: DataFlavor): Boolean = flavor == DataFlavor.imageFlavor

        @Throws(UnsupportedFlavorException::class, IOException::class)
        override fun getTransferData(flavor: DataFlavor): Any {
            if (flavor != DataFlavor.imageFlavor) throw UnsupportedFlavorException(flavor)
            return image
        }
    }
}