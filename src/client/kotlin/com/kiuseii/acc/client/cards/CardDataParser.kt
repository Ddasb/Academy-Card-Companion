package com.kiuseii.acc.client.cards

import net.minecraft.nbt.ByteTag
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.FloatTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.LongTag
import net.minecraft.nbt.ShortTag
import net.minecraft.nbt.Tag

data class CardStatValue(
    val source: String,
    val value: Number,
    val minInclusive: Number,
    val maxInclusive: Number
)

object CardDataParser {
    fun extractGrade(root: CompoundTag): Byte {
        return root.getByte("grade")
    }

    fun extractSourceValues(root: CompoundTag): List<CardStatValue> {
        val modifiersList = root.getList("modifiers", Tag.TAG_COMPOUND.toInt())

        return modifiersList.indices.mapNotNull { i ->
            val entry = modifiersList.getCompound(i)
            val source = entry.getString("source")

            val subModifiers = entry
                .getCompound("attribute")
                .getCompound("value")
                .getList("modifiers", Tag.TAG_COMPOUND.toInt())

            val assignTag = subModifiers.indices
                .map { subModifiers.getCompound(it) }
                .firstOrNull { it.getString("type") == "assign" }

            val assignValue = assignTag?.getCompound("value")
            val config = assignValue?.getCompound("config")

            val value = assignValue?.get("value")?.let { extractNumber(it) }
            val min = config?.get("minimum_inclusive")?.let { extractNumber(it) }
            val max = config?.get("maximum_inclusive")?.let { extractNumber(it) }

            if (value != null && min != null && max != null) {
                CardStatValue(source, value, min, max)
            } else {
                null
            }
        }
    }

    private fun extractNumber(tag: Tag): Number = when (tag) {
        is FloatTag -> tag.asFloat
        is DoubleTag -> tag.asDouble
        is IntTag -> tag.asInt
        is LongTag -> tag.asLong
        is ShortTag -> tag.asShort
        is ByteTag -> tag.asByte
        else -> 0.0
    }
}