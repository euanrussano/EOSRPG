package com.sophia.eosrpg.model.item

class DamageItemComponent(
    val minimumDamage : Int,
    val maximumDamage : Int,
    val damagePriceMultiplier : Float = 1f
) : ItemComponent {
    override fun getPriceMultiplier(): Float {
        return damagePriceMultiplier
    }
}
