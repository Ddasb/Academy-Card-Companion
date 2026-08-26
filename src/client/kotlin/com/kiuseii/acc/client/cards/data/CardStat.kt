package com.kiuseii.acc.client.cards.data

data class CardStatDefinition(
    val key: String,
    val percent: Boolean,
    val color: String = ""
)

object CardStat {
    private val definitions: Map<String, CardStatDefinition> = mapOf(
        "shiny_chance" to CardStatDefinition(
            key = "text.academy.card.display.shiny_chance_increased",
            percent = true,
            color = "#ffd700"
        ),
        "capture_friendship" to CardStatDefinition(
            key = "text.academy.card.display.capture_friendship_increased",
            percent = true,
            color = "#ff69b4"
        ),
        "ev_yield/cobblemon:hp" to CardStatDefinition(
            key = "text.academy.card.display.ev_yield_cobblemon:hp_increased",
            percent = true,
            color = "#4caf50"
        ),
        "ev_yield/cobblemon:attack" to CardStatDefinition(
            key = "text.academy.card.display.ev_yield_cobblemon:attack_increased",
            percent = true,
            color = "#ffeb3b"
        ),
        "ev_yield/cobblemon:defence" to CardStatDefinition(
            key = "text.academy.card.display.ev_yield_cobblemon:defence_increased",
            percent = true,
            color = "#ff9800"
        ),
        "ev_yield/cobblemon:special_attack" to CardStatDefinition(
            key = "text.academy.card.display.ev_yield_cobblemon:special_attack_increased",
            percent = true,
            color = "#4fc3f7"
        ),
        "ev_yield/cobblemon:special_defence" to CardStatDefinition(
            key = "text.academy.card.display.ev_yield_cobblemon:special_defence_increased",
            percent = true,
            color = "#1565c0"
        ),
        "ev_yield/cobblemon:speed" to CardStatDefinition(
            key = "text.academy.card.display.ev_yield_cobblemon:speed_increased",
            percent = true,
            color = "#c71585"
        ),
        "label_weight/dark" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_dark_increased",
            percent = true,
            color = "#705848"
        ),
        "label_weight/psychic" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_psychic_increased",
            percent = true,
            color = "#f85888"
        ),
        "label_weight/steel" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_steel_increased",
            percent = true,
            color = "#b8b8d0"
        ),
        "label_weight/flying" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_flying_increased",
            percent = true,
            color = "#a890f0"
        ),
        "label_weight/fire" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_fire_increased",
            percent = true,
            color = "#f08030"
        ),
        "label_weight/poison" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_poison_increased",
            percent = true,
            color = "#a040a0"
        ),
        "label_weight/water" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_water_increased",
            percent = true,
            color = "#6890f0"
        ),
        "label_weight/electric" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_electric_increased",
            percent = true,
            color = "#f8d030"
        ),
        "label_weight/fighting" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_fighting_increased",
            percent = true,
            color = "#c03028"
        ),
        "label_weight/ice" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_ice_increased",
            percent = true,
            color = "#98d8d8"
        ),
        "label_weight/ghost" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_ghost_increased",
            percent = true,
            color = "#705898"
        ),
        "label_weight/bug" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_bug_increased",
            percent = true,
            color = "#a8b820"
        ),
        "label_weight/grass" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_grass_increased",
            percent = true,
            color = "#78c850"
        ),
        "label_weight/ground" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_ground_increased",
            percent = true,
            color = "#e0c068"
        ),
        "label_weight/fairy" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_fairy_increased",
            percent = true,
            color = "#ee99ac"
        ),
        "label_weight/normal" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_normal_increased",
            percent = true,
            color = "#a8a878"
        ),
        "label_weight/dragon" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_dragon_increased",
            percent = true,
            color = "#7038f8"
        ),
        "label_weight/rock" to CardStatDefinition(
            key = "text.academy.card.display.label_weight_rock_increased",
            percent = true,
            color = "#b8a038"
        ),
        "capture_chance" to CardStatDefinition(
            key = "text.academy.card.display.capture_chance_increased",
            percent = true,
            color = "#4caf50"
        ),
        "capture_experience" to CardStatDefinition(
            key = "text.academy.card.display.capture_experience_increased",
            percent = true,
            color = "#8bc34a"
        ),
        "minecraft:player.block_interaction_range" to CardStatDefinition(
            key = "text.academy.card.display.vanilla_minecraft_player_block_interaction_range_add_value",
            percent = false,
            color = "#b0bec5"
        ),
        "minecraft:player.block_break_speed" to CardStatDefinition(
            key = "text.academy.card.display.vanilla_minecraft_player_block_break_speed_add_multiplied_base",
            percent = true,
            color = "#ff9800"
        ),
        "minecraft:generic.movement_speed" to CardStatDefinition(
            key = "text.academy.card.display.vanilla_minecraft_generic_movement_speed_add_multiplied_base",
            percent = true,
            color = "#03a9f4"
        ),
        "minecraft:generic.jump_strength" to CardStatDefinition(
            key = "text.academy.card.display.vanilla_minecraft_generic_jump_strength_add_multiplied_total",
            percent = false,
            color = "#00bcd4"
        ),
        "minecraft:generic.water_movement_efficiency" to CardStatDefinition(
            key = "text.academy.card.display.vanilla_minecraft_generic_water_movement_efficiency_add_multiplied_base",
            percent = true,
            color = "#2196f3"
        ),
        "minecraft:generic.oxygen_bonus" to CardStatDefinition(
            key = "text.academy.card.display.vanilla_minecraft_generic_oxygen_bonus_add_value",
            percent = false,
            color = "#81d4fA"
        ),
        "minecraft:generic.armor" to CardStatDefinition(
            key = "text.academy.card.display.vanilla_minecraft_generic_armor_add_value",
            percent = false,
            color = "#9e9e9e"
        )
    )

    private val multipliers : Map<Int, Double> = mapOf(
        1 to 0.40,
        2 to 0.70,
        3 to 1.00,
        4 to 1.10,
        5 to 1.20,
        6 to 1.30,
        7 to 1.40,
        8 to 2.00,
        9 to 2.50,
        10 to 3.00
    )

    private val gradients: Map<String, Pair<Int, Int>> = mapOf(
        "COMMON" to (0x6B5844 to 0xA8927A),
        "UNCOMMON" to (0xA0622D to 0xF0B080),
        "RARE" to (0x7A8B99 to 0xB0B8BF),
        "EPIC" to (0xB98A88 to 0xE0C4B0),
        "LEGENDARY" to (0xFF9800 to 0xFFEB3B),
        "SHINY" to (0xB3E5FC to 0xFFB3D9)
    )

    fun get(source: String): CardStatDefinition? {
        definitions[source]?.let { return it }

        val withoutTier = source.replace(Regex("_t\\d+$"), "")
        return definitions[withoutTier]
    }

    fun getMultiplier(grade: Int): Double {
        return multipliers[grade] ?: 1.0
    }

    fun getGradients(rarity: String): Pair<Int, Int> {
        return gradients[rarity.uppercase()] ?: (0xFFFFFF to 0xFFFFFF)
    }
}