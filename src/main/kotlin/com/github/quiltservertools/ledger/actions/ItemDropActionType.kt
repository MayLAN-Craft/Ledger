package com.github.quiltservertools.ledger.actions

import net.minecraft.server.MinecraftServer

open class ItemDropActionType : AbstractItemActionType() {
    override val identifier = "item-drop"

    override fun rollback(server: MinecraftServer) = removeItem(server, objectState)

    override fun restore(server: MinecraftServer) = createItem(server, objectState)
}
