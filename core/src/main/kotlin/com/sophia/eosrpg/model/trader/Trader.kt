package com.sophia.eosrpg.model.trader

import com.sophia.eosrpg.model.Entity
import com.sophia.eosrpg.model.InventoryHolderComponent

class Trader(
    val name : String,
) : Entity() {

    init {
        add(InventoryHolderComponent(this))
    }
}
