package com.sophia.eosrpg.model.item

import com.sophia.eosrpg.model.entity.Entity

interface ItemComponent {
    var parent : Item?
    fun getPriceMultiplier() : Float
    abstract fun performAction(actor: Entity, target: Entity)
}
