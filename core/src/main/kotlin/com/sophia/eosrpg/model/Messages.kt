package com.sophia.eosrpg.model

import com.sophia.eosrpg.model.item.ItemInstance
import com.sophia.eosrpg.model.recipe.Recipe

object Messages {
    var value = 0
    fun nextInt() = value++

    // -------INVENTORY EVENTS-------------------
    class ItemInstanceAddedEvent(val owner: Entity, val itemInstance: ItemInstance) {
        companion object {
            val code: Int = nextInt()
        }

    }
    class ItemInstanceRemovedEvent(val owner: Entity, val itemInstance: ItemInstance) {
        companion object {
            val code: Int = nextInt()
        }

    }
    // -------END INVENTORY EVENTS-------------------
    // -------ITEM EVENTS--------------------------
    data class MissedAttackEvent(val actor: Entity, val target: Entity) {
        companion object {
            val code: Int = nextInt()
        }
    }

    class EntityAttackEvent(val actor: Entity, val target: Entity) {
        companion object {
            val code: Int = nextInt()
        }
    }
    // ----_END ITEM EVENTS

    //------------ ENTITY EVENT ------------------
    class EntityWasHitEvent(val owner: Entity, val damage: Int) {
        companion object {
            val code: Int = nextInt()
        }
    }

    class EntityWasKilledEvent(val owner: Entity) {
        companion object {
            val code: Int = nextInt()
        }
    }

    class EntityWasFullyHealed(val owner: Entity) {
        companion object {
            val code: Int = nextInt()
        }
    }
    class EntityWasHealed(val owner : Entity, val amount : Int){
        companion object{
            val code : Int = nextInt()
        }
    }
    //------------ ENTITY EVENT END ------------------
    class HeroLearntRecipeEvent(val hero: Hero, val recipe: Recipe) {
        companion object{
            val code : Int = nextInt()
        }
    }

}
