package com.kiuseii.acc.client.album.tooltip

import com.kiuseii.acc.client.album.data.CardAlbumDataParser
import com.kiuseii.acc.client.album.data.CardAlbumStatAggregator
import com.kiuseii.acc.client.cards.tooltip.CardTooltipFormater
import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

object CardAlbumTooltipHandler {
    private val ALBUM_ITEM_ID = ResourceLocation.fromNamespaceAndPath("academy", "card_album")
    private val CONTAINER_COMPONENT_ID = ResourceLocation.fromNamespaceAndPath("academy", "card_album_container")

    fun register() {
        ItemTooltipCallback.EVENT.register { stack, _, _, lines ->
            val itemId = BuiltInRegistries.ITEM.getKey(stack.item)
            if (itemId != ALBUM_ITEM_ID) return@register

            val containerTag = findContainerTag(stack) ?: return@register
            val slots = CardAlbumDataParser.extractSlots(containerTag)

            val totals = CardAlbumStatAggregator.aggregate(slots)
            if (totals.isEmpty()) return@register

            lines.add(Component.literal(""))
            lines.add(Component.translatable("text.acc.card_album.total_bonus"))

            for (statValue in totals) {
                lines.add(CardTooltipFormater.formatModifier(statValue, grade = 3))
            }

            lines.add(Component.literal(""))
            lines.add(formatDetail())
        }
    }

    private fun findContainerTag(stack: ItemStack): CompoundTag? {
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

    fun formatDetail(): Component {
        return if (!Screen.hasShiftDown()) {
            Component.translatable("text.acc.card.display.shift_hint_show_ranges")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
        } else {
            Component.translatable("text.acc.card.display.shift_hint_hide_ranges")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
        }
    }
}