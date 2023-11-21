package com.sophia.eosrpg.model

import com.badlogic.gdx.ai.msg.MessageManager
import kotlin.math.min

class LivingEntityComponent(val owner : Entity, private var maximumHitPoints : Int) : EntityComponent {
    private var __hitPoints = maximumHitPoints
    val hitPoints : Int
        get() = __hitPoints
    val isDead : Boolean
        get() = __hitPoints <= 0

    fun takeDamage(damage: Int) {
        __hitPoints -= damage
        val event = Messages.EntityWasHitEvent(owner, damage)
        val code = Messages.EntityWasHitEvent.code
        MessageManager.getInstance().dispatchMessage(code, event)

        if (__hitPoints <= 0){
            __hitPoints = 0
            val event2 = Messages.EntityWasKilledEvent(owner)
            val code2 = Messages.EntityWasKilledEvent.code
            MessageManager.getInstance().dispatchMessage(code2, event2)

        }
    }
    fun fullyHeal() {
        __hitPoints = maximumHitPoints
        val event = Messages.EntityWasFullyHealed(owner)
        val code = Messages.EntityWasFullyHealed.code
        MessageManager.getInstance().dispatchMessage(code, event)
    }
    fun heal(amount : Int) {
        __hitPoints = min(__hitPoints + amount, maximumHitPoints)
        val event = Messages.EntityWasHealed(owner, amount)
        val code = Messages.EntityWasHealed.code
        MessageManager.getInstance().dispatchMessage(code, event)
    }

    companion object {
        fun get(target: Entity): LivingEntityComponent {
            return target.components.first { it is LivingEntityComponent } as LivingEntityComponent
        }
    }

}
