package com.sophia.eosrpg.model.item

import com.sophia.eosrpg.model.entity.Entity

class RecoverHealthItemComponent(val amountToRecover : Int) : ItemComponent {
    override var parent: Item? = null
    override fun getPriceMultiplier(): Float {
        return 1f
    }

    override fun performAction(actor: Entity, target: Entity) {
        target.health.heal(2)
    }
}
