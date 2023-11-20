package com.sophia.eosrpg.model

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.sophia.eosrpg.model.item.CompositeItem
import com.sophia.eosrpg.model.item.DamageItem
import com.sophia.eosrpg.model.item.ItemInstance

class Inventory(val owner : InventoryHolder) : Telegraph {

    private val __itemInstances = mutableListOf<ItemInstance>()
    val itemInstances : List<ItemInstance>
        get() = __itemInstances
    val weapons : List<ItemInstance>
        get() = __itemInstances.filter { itemInstance ->
            val item = itemInstance.item
            if (item is CompositeItem){
                return@filter item.has(DamageItem::class)
            }else{
                false
            }
        }

    fun addItemInstanceToInventory(itemInstance: ItemInstance) {
        __itemInstances.add(itemInstance)
        val code = Messages.ItemInstanceAddedEvent.code
        val event = Messages.ItemInstanceAddedEvent(this, itemInstance)
        MessageManager.getInstance().dispatchMessage(this, code, event)
    }

    fun removeItemInstanceToInventory(itemInstance: ItemInstance) {
        val isRemoved = __itemInstances.remove(itemInstance)
        if (isRemoved){
            val code = Messages.ItemInstanceRemovedEvent.code
            val event = Messages.ItemInstanceRemovedEvent(this, itemInstance)
            MessageManager.getInstance().dispatchMessage(this, code, event)
        }
    }
    fun hasAllItems(itemQuantity : Map<String, Int>) : Boolean{
        for ((itemName, qty) in itemQuantity){
            val count = __itemInstances.count { itemInstance -> itemInstance.item.name == itemName }
            if (count < qty) return false
        }
        return true
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        TODO("Not yet implemented")
    }

    fun findByName(itemName: String): ItemInstance {
        return __itemInstances.first { itemInstance -> itemInstance.item.name == itemName }
    }
}
