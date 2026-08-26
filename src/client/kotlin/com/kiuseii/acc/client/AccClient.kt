package com.kiuseii.acc.client

import com.kiuseii.acc.client.album.CardAlbumClientTooltip
import com.kiuseii.acc.client.album.CardAlbumTooltip
import com.kiuseii.acc.client.album.CardAlbumTooltipHandler
import com.kiuseii.acc.client.cards.CardTooltipHandler
import com.kiuseii.acc.client.commands.LogItemCommand
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback

object AccClient : ClientModInitializer {
	override fun onInitializeClient() {
		LogItemCommand.register()

		CardTooltipHandler.register()
		CardAlbumTooltipHandler.register()

		TooltipComponentCallback.EVENT.register(TooltipComponentCallback { data ->
			if (data is CardAlbumTooltip) CardAlbumClientTooltip(data.slots) else null
		})
	}
}