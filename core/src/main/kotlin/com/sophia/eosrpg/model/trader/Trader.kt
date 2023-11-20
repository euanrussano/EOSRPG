package com.sophia.eosrpg.model.trader

import com.sophia.eosrpg.model.Inventory
import com.sophia.eosrpg.model.InventoryHolder
import com.sophia.eosrpg.model.item.ItemInstance

class Trader(
    val name : String,
) : InventoryHolder {
//    var listener : TraderListener? = null
//
//    interface TraderListener {
//        fun itemInstanceAddedToInventory(trader: Trader, itemInstance: ItemInstance)
//        fun itemInstanceRemovedFromInventory(trader: Trader, itemInstance: ItemInstance)
//
//    }
    override val inventory = Inventory(this)

//    private val __inventory : MutableList<ItemInstance> = mutableListOf()
//    val inventory : List<ItemInstance>
//        get() = __inventory
//
//    fun addItemToInventory(itemInstance: ItemInstance) {
//        __inventory.add(itemInstance)
//        listener?.itemInstanceAddedToInventory(this, itemInstance)
//    }
//    fun removeItemFromInventory(itemInstance: ItemInstance) {
//        val isRemoved = __inventory.remove(itemInstance)
//        if (isRemoved){
//            listener?.itemInstanceRemovedFromInventory(this, itemInstance)
//        }
//    }
}
