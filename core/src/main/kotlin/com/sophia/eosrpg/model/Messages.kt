package com.sophia.eosrpg.model

import com.sophia.eosrpg.model.item.ItemInstance

object Messages {
    var value = 0
    fun nextInt() = value++

    // -------INVENTORY EVENTS-------------------
    class ItemInstanceAddedEvent(val inventory: Inventory, val itemInstance: ItemInstance) {
        companion object {
            val code: Int = nextInt()
        }

    }
    class ItemInstanceRemovedEvent(val inventory: Inventory, val itemInstance: ItemInstance) {
        companion object {
            val code: Int = nextInt()
        }

    }
    // -------END INVENTORY EVENTS-------------------

}
