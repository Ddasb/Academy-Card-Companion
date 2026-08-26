package com.kiuseii.acc.client.album

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag

data class CardAlbumSlot(
    val index: Int,
    val count: Int,
    val cardTag: CompoundTag
)

object CardAlbumDataParser {
    fun extractSlots(root: CompoundTag): List<CardAlbumSlot> {
        val items = root.getList("items", Tag.TAG_COMPOUND.toInt())
        val slots = mutableListOf<CardAlbumSlot>()

        for (i in items.indices) {
            val entry = items.getCompound(i)
            val index = entry.getByte("index").toInt()
            val count = if (entry.contains("count")) entry.getByte("count").toInt() else 1

            val nested = entry.getCompound("nbt")
            val cardTag = nested.getCompound("academy:card")

            if (cardTag.isEmpty) continue

            slots.add(CardAlbumSlot(index, count, cardTag))
        }

        return slots.sortedBy { it.index }
    }
}