package com.sophia.eosrpg.model.item

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.math.MathUtils
import com.sophia.eosrpg.model.entity.Entity
import com.sophia.eosrpg.model.entity.Health
import com.sophia.eosrpg.model.Messages
import com.sophia.eosrpg.model.skill.Skill

class DamageItemComponent(
    val minimumDamage : Int,
    val maximumDamage : Int,
    val damagePriceMultiplier : Float = 1f,
    val skillToUse : Skill
) : ItemComponent {
    override var parent: Item? = null
    override fun getPriceMultiplier(): Float {
        return damagePriceMultiplier
    }

    override fun performAction(actor: Entity, target: Entity) {
        val roll = MathUtils.random(3, 18)
        val skillTest = actor.skills[skillToUse] ?: skillToUse.getPreDefinedValue(actor)

        if (roll > skillTest){
            val event = Messages.MissedAttackEvent(actor, target)
            val code = Messages.MissedAttackEvent.code
            MessageManager.getInstance().dispatchMessage(code, event)
            return
        }

        val damageToTarget = MathUtils.random(minimumDamage, maximumDamage)
        if (damageToTarget == 0){
            val event = Messages.MissedAttackEvent(actor, target)
            val code = Messages.MissedAttackEvent.code
            MessageManager.getInstance().dispatchMessage(code, event)

        } else {
            val event = Messages.EntityAttackEvent(actor, target)
            val code = Messages.EntityAttackEvent.code
            MessageManager.getInstance().dispatchMessage(code, event)

            val livingComponent : Health = target.health
            livingComponent.takeDamage(damageToTarget)
        }

    }
}
