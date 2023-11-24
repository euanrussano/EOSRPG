package com.sophia.eosrpg.model

import com.sophia.eosrpg.model.entity.Entity
import com.sophia.eosrpg.model.item.ItemInstance
import com.sophia.eosrpg.model.location.Location
import com.sophia.eosrpg.model.monster.MonsterInstance
import com.sophia.eosrpg.model.quest.Quest
import com.sophia.eosrpg.model.recipe.Recipe
import com.sophia.eosrpg.model.trader.Trader

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

    //------------ HERO EVENT END ------------------
    class HeroLearntRecipeEvent(val hero: Hero, val recipe: Recipe) {
        companion object{
            val code : Int = nextInt()
        }
    }

    class HeroReceivedQuestEvent(val hero: Hero, val quest: Quest) {
        companion object{
            val code : Int = nextInt()
        }
    }

    class HeroCompletedQuestEvent(val hero: Hero, val quest: Quest) {
        companion object{
            val code : Int = nextInt()
        }
    }

    class HeroReceivedGoldEvent(val hero: Hero, val amount: Int) {
        companion object{
            val code : Int = nextInt()
        }
    }

    class HeroSpentGoldEvent(val hero: Hero, val amount: Int) {
        companion object{
            val code : Int = nextInt()
        }
    }

    class HeroGainedXP(val hero: Hero, val amount: Int) {
        companion object{
            val code : Int = nextInt()
        }
    }

    class HeroGainedLevel(val hero: Hero) {
        companion object{
            val code : Int = nextInt()
        }
    }

    class CurrentHeroChangedEvent(val hero: Hero?) {
        companion object{
            val code : Int = nextInt()
        }
    }

    class CurrentLocationChangedEvent(val location: Location?) {
        companion object{
            val code : Int = nextInt()
        }
    }

    class CurrentMonsterInstanceChangedEvent(val monsterInstance: MonsterInstance?) {
        companion object{
            val code : Int = nextInt()
        }
    }

    class CurrentTraderChangedEvent(val trader: Trader?) {
        companion object{
            val code : Int = nextInt()
        }
    }


}
