package com.kiuseii.acc.client

import com.kiuseii.acc.client.album.tooltip.CardAlbumClientTooltip
import com.kiuseii.acc.client.album.tooltip.CardAlbumTooltip
import com.kiuseii.acc.client.album.tooltip.CardAlbumTooltipHandler
import com.kiuseii.acc.client.cards.tooltip.CardTooltipHandler
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback

object AccClient : ClientModInitializer {
	override fun onInitializeClient() {
		CardTooltipHandler.register()
		CardAlbumTooltipHandler.register()

		TooltipComponentCallback.EVENT.register(TooltipComponentCallback { data ->
			if (data is CardAlbumTooltip) CardAlbumClientTooltip(data.slots) else null
		})
	}
}