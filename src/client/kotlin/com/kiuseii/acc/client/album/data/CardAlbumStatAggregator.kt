package com.kiuseii.acc.client.album.data

import com.kiuseii.acc.client.cards.data.CardDataParser
import com.kiuseii.acc.client.cards.data.CardStat
import com.kiuseii.acc.client.cards.data.CardStatValue

object CardAlbumStatAggregator {

    private val TIER_SUFFIX = Regex("_t\\d+$")

    private class Accumulator {
        var total = 0.0
        var min = 0.0
        var max = 0.0
    }

    fun aggregate(slots: List<CardAlbumSlot>): List<CardStatValue> {
        val totals = linkedMapOf<String, Accumulator>()

        for (slot in slots) {
            val cardTag = slot.cardTag
            val grade = CardDataParser.extractGrade(cardTag)
            val multiplier = CardStat.getMultiplier(grade.toInt())

            for (statValue in CardDataParser.extractSourceValues(cardTag)) {
                if (CardStat.get(statValue.source) == null) continue

                val canonicalSource = statValue.source.replace(TIER_SUFFIX, "")
                val acc = totals.getOrPut(canonicalSource) { Accumulator() }

                acc.total += statValue.value.toDouble() * multiplier
                acc.min += statValue.minInclusive.toDouble() * multiplier
                acc.max += statValue.maxInclusive.toDouble() * multiplier
            }
        }

        return totals.map { (source, acc) -> CardStatValue(source, acc.total, acc.min, acc.max) }
    }
}