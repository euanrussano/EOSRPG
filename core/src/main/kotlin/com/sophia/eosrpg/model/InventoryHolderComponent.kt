package com.sophia.eosrpg.model

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.eosrpg.model.item.DamageItemComponent
import com.sophia.eosrpg.model.item.ItemInstance

class InventoryHolderComponent(val owner: Entity) : EntityComponent {
    private val __itemInstances = mutableListOf<ItemInstance>()
    val itemInstances : List<ItemInstance>
        get() = __itemInstances
    val weapons : List<ItemInstance>
        get() = __itemInstances.filter { itemInstance -> itemInstance.item.has(DamageItemComponent::class) }

    fun addItemInstanceToInventory(itemInstance: ItemInstance) {
        __itemInstances.add(itemInstance)
        val code = Messages.ItemInstanceAddedEvent.code
        val event = Messages.ItemInstanceAddedEvent(owner, itemInstance)
        MessageManager.getInstance().dispatchMessage(code, event)
    }

    fun removeItemInstanceToInventory(itemInstance: ItemInstance) {
        val isRemoved = __itemInstances.remove(itemInstance)
        if (isRemoved){
            val code = Messages.ItemInstanceRemovedEvent.code
            val event = Messages.ItemInstanceRemovedEvent(owner, itemInstance)
            MessageManager.getInstance().dispatchMessage(code, event)
        }
    }
    fun hasAllItems(itemQuantity : Map<String, Int>) : Boolean{
        for ((itemName, qty) in itemQuantity){
            val count = __itemInstances.count { itemInstance -> itemInstance.item.name == itemName }
            if (count < qty) return false
        }
        return true
    }


    fun findByName(itemName: String): ItemInstance {
        return __itemInstances.first { itemInstance -> itemInstance.item.name == itemName }
    }

    companion object {
        fun get(target: Entity): InventoryHolderComponent {
            return target.components.first { it is InventoryHolderComponent } as InventoryHolderComponent
        }
    }

}
