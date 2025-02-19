package com.github.quiltservertools.ledger.actions

import com.github.quiltservertools.ledger.utility.NbtUtils
import com.github.quiltservertools.ledger.utility.TextColorPallet
import com.github.quiltservertools.ledger.utility.UUID
import com.github.quiltservertools.ledger.utility.getUuid
import com.github.quiltservertools.ledger.utility.getWorld
import com.github.quiltservertools.ledger.utility.literal
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ItemEntity
import net.minecraft.nbt.StringNbtReader
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text

abstract class AbstractItemActionType : AbstractActionType() {
    // Not used
    override fun getTranslationType(): String = "item"

    override fun getObjectMessage(source: ServerCommandSource): Text {
        val stack = getStack(source.server)

        return "${stack.count} ".literal().append(
            stack.itemName
        ).setStyle(TextColorPallet.secondaryVariant).styled {
            it.withHoverEvent(
                HoverEvent.ShowItem(
                    stack
                )
            )
        }
    }

    private fun getStack(server: MinecraftServer) = NbtUtils.itemFromProperties(
        extraData, objectIdentifier, server.registryManager
    )

    protected fun createItem(server: MinecraftServer, state: String?): Boolean {
        val world = server.getWorld(world)

        val objectStateCompound = StringNbtReader.readCompound(state)
        val uuid = objectStateCompound!!.getUuid(UUID)
        val entity = world?.getEntity(uuid)

        if (entity == null) {
            val newEntity = ItemEntity(EntityType.ITEM, world)
            newEntity.readNbt(objectStateCompound)
            world?.spawnEntity(newEntity)
        }
        return true
    }

    protected fun removeItem(server: MinecraftServer, state: String?): Boolean {
        val world = server.getWorld(world)

        val newEntity = StringNbtReader.readCompound(state)
        val uuid = newEntity!!.getUuid(UUID)
        val entity = world?.getEntity(uuid)

        if (entity != null) {
            entity.remove(Entity.RemovalReason.DISCARDED)
            return true
        }
        return false
    }
}