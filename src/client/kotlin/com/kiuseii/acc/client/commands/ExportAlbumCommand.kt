package com.kiuseii.acc.client.commands

import com.kiuseii.acc.client.album.data.CardAlbumDataParser
import com.kiuseii.acc.client.album.export.CardAlbumExport
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

object ExportAlbumCommand {
    private val ALBUM_ITEM_ID = ResourceLocation.fromNamespaceAndPath("academy", "card_album")

    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal("acc")
                    .then(
                        ClientCommandManager.literal("exportBinder").executes { ctx ->
                            val minecraft = Minecraft.getInstance()
                            val player = ctx.source.player
                            val stack = player.mainHandItem

                            val itemId = BuiltInRegistries.ITEM.getKey(stack.item)
                            if (itemId != ALBUM_ITEM_ID) {
                                ctx.source.sendFeedback(Component.literal("Tiens un Card Album en main pour l'exporter."))
                                return@executes 0
                            }

                            val containerTag = CardAlbumExport.findContainerTag(stack)
                            if (containerTag == null) {
                                ctx.source.sendFeedback(Component.literal("Impossible de lire le contenu de ce Card Album."))
                                return@executes 0
                            }

                            val slots = CardAlbumDataParser.extractSlots(containerTag)
                            CardAlbumExport.exportImage(minecraft, slots)

                            1
                        }
                    )
            )
        }
    }
}