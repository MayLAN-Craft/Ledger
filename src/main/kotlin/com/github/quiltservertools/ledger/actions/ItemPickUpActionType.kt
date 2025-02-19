package com.github.quiltservertools.ledger.actions

import net.minecraft.server.MinecraftServer

open class ItemPickUpActionType : AbstractItemActionType() {
    override val identifier = "item-pick-up"

    override fun rollback(server: MinecraftServer) = createItem(server, oldObjectState)

    override fun restore(server: MinecraftServer) = removeItem(server, oldObjectState)
}
