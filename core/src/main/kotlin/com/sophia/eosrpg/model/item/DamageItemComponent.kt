package com.sophia.eosrpg.model.item

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.math.MathUtils
import com.sophia.eosrpg.model.Entity
import com.sophia.eosrpg.model.LivingEntityComponent
import com.sophia.eosrpg.model.Messages
import kotlin.math.max

class DamageItemComponent(
    val minimumDamage : Int,
    val maximumDamage : Int,
    val damagePriceMultiplier : Float = 1f
) : ItemComponent {
    override fun getPriceMultiplier(): Float {
        return damagePriceMultiplier
    }

    override fun performAction(actor: Entity, target: Entity) {
        val damageToTarget = MathUtils.random(minimumDamage, maximumDamage)
        if (damageToTarget == 0){
            val event = Messages.MissedAttackEvent(actor, target)
            val code = Messages.MissedAttackEvent.code
            MessageManager.getInstance().dispatchMessage(code, event)

        } else {
            val event = Messages.EntityAttackEvent(actor, target)
            val code = Messages.EntityAttackEvent.code
            MessageManager.getInstance().dispatchMessage(code, event)

            val livingComponent : LivingEntityComponent = LivingEntityComponent.get(target)
            livingComponent.takeDamage(damageToTarget)
        }

    }
}
