package com.sophia.eosrpg.model.item

import com.sophia.eosrpg.model.Entity

interface ItemComponent {
    fun getPriceMultiplier() : Float
    abstract fun performAction(actor: Entity, target: Entity)
}
