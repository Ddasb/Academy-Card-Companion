package com.kiuseii.acc.client.mixin

import com.kiuseii.acc.client.album.CardAlbumDataParser
import com.kiuseii.acc.client.album.CardAlbumTooltip
import com.mojang.serialization.Codec
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Unique
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.Optional

@Mixin(Item::class)
abstract class CardAlbumTooltipImageMixin {
    @Unique
    private val academyCardCompanion_albumItemId: ResourceLocation =
        ResourceLocation.fromNamespaceAndPath("academy", "card_album")

    @Unique
    private val academyCardCompanion_containerComponentId: ResourceLocation =
        ResourceLocation.fromNamespaceAndPath("academy", "card_album_container")

    @Inject(method = ["getTooltipImage"], at = [At("HEAD")], cancellable = true)
    private fun injectAlbumTooltipImage(
        stack: ItemStack,
        cir: CallbackInfoReturnable<Optional<TooltipComponent>>
    ) {
        val itemId = BuiltInRegistries.ITEM.getKey(stack.item)
        if (itemId != academyCardCompanion_albumItemId) return

        val containerTag = academyCardCompanion_findContainerTag(stack) ?: return
        val slots = CardAlbumDataParser.extractSlots(containerTag)

        cir.returnValue = Optional.of(CardAlbumTooltip(slots))
    }

    @Unique
    private fun academyCardCompanion_findContainerTag(stack: ItemStack): CompoundTag? {
        for (typedComponent in stack.components) {
            val type = typedComponent.type()
            val id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type) ?: continue
            if (id != academyCardCompanion_containerComponentId) continue

            @Suppress("UNCHECKED_CAST")
            val codec = type.codec() as? Codec<Any> ?: return null

            val result = codec.encodeStart(NbtOps.INSTANCE, typedComponent.value())
            return result.result().orElse(null) as? CompoundTag
        }

        return null
    }
}