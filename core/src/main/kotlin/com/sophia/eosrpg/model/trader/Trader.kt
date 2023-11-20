package com.sophia.eosrpg.model.trader

import com.sophia.eosrpg.model.item.ItemInstance

class Trader(
    val name : String,
) {

    private val __inventory : MutableList<ItemInstance> = mutableListOf()

    fun addItemToInventory(itemInstance: ItemInstance) {
        __inventory.add(itemInstance)
    }
    fun removeItemFromInventory(itemInstance: ItemInstance) {
        val isRemoved = __inventory.remove(itemInstance)
    }
}
