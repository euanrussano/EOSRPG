package com.sophia.eosrpg.model.item

import com.sophia.eosrpg.model.Entity
import com.sophia.eosrpg.model.LivingEntityComponent

class RecoverHealthItemComponent(val amountToRecover : Int) : ItemComponent {
    override var parent: Item? = null
    override fun getPriceMultiplier(): Float {
        return 1f
    }

    override fun performAction(actor: Entity, target: Entity) {
        LivingEntityComponent.get(target).heal(2)
    }
}
