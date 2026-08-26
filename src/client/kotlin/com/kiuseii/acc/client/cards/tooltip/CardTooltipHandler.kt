package com.kiuseii.acc.client.cards.tooltip
import com.kiuseii.acc.client.cards.data.CardDataParser
import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

object CardTooltipHandler {
    private val CARD_ITEM_ID = ResourceLocation.fromNamespaceAndPath("academy", "card")

    fun register() {
        ItemTooltipCallback.EVENT.register { stack, context, tooltipType, lines ->
            val itemId = BuiltInRegistries.ITEM.getKey(stack.item)

            if (itemId != CARD_ITEM_ID) {
                return@register
            }

            val cardComponent = stack.components.firstOrNull { it.type().toString() == "academy:card" }
                ?: return@register

            val type = cardComponent.type()
            val value = cardComponent.value()

            @Suppress("UNCHECKED_CAST")
            val codec: Codec<Any>? = type.codec() as? Codec<Any>

            val nbtElement = codec?.encodeStart(NbtOps.INSTANCE, value)?.result()?.orElse(null)

            lines.clear()

            if (nbtElement is CompoundTag) {
                val grade = CardDataParser.extractGrade(nbtElement).toInt()

                lines.add(stack.hoverName.copy())
                lines.add(CardTooltipFormater.formatRarity(nbtElement.getString("rarity")))
                lines.add(CardTooltipFormater.formatGrade(grade))

                CardDataParser.extractSourceValues(nbtElement).forEach { card ->
                    lines.add(CardTooltipFormater.formatModifier(card, grade))
                }

                lines.add(Component.literal(""))
                lines.add(CardTooltipFormater.formatDetail())
            }
        }
    }
}