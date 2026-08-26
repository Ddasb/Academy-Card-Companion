package com.kiuseii.acc.client.commands

import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.nbt.NbtOps
import net.minecraft.network.chat.Component

object LogItemCommand {
    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal("logitem").executes { ctx ->
                    val player = ctx.source.player
                    val stack = player.mainHandItem

                    if (stack.isEmpty) {
                        ctx.source.sendFeedback(Component.literal("Aucun item en main."))
                        return@executes 1
                    }

                    ctx.source.sendFeedback(Component.literal("Item: ${stack.item}"))
                    ctx.source.sendFeedback(Component.literal("Count: ${stack.count}"))

                    for (typedComponent in stack.components) {
                        val type = typedComponent.type()
                        val value = typedComponent.value()

                        @Suppress("UNCHECKED_CAST")
                        val codec: Codec<Any>? = type.codec() as? Codec<Any>

                        if (codec == null) {
                            ctx.source.sendFeedback(Component.literal(" - $type => (non-persistant, pas de codec)"))
                            continue
                        }

                        val result = codec.encodeStart(NbtOps.INSTANCE, value)

                        result.result().ifPresentOrElse(
                            { nbtElement ->
                                ctx.source.sendFeedback(Component.literal(" - $type => $nbtElement"))
                            },
                            {
                                ctx.source.sendFeedback(Component.literal(" - $type => (échec d'encodage)"))
                            }
                        )
                    }

                    1
                }
            )
        }
    }
}