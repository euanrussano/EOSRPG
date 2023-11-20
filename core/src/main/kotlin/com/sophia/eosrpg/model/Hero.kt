package com.sophia.eosrpg.model

import com.sophia.eosrpg.model.item.ItemInstance
import com.sophia.eosrpg.model.item.Item
import com.sophia.eosrpg.model.quest.Quest

class Hero(
    name : String,
    characterClass : String,
    hitPoints : Int,
    experiencePoints : Int,
    level : Int,
    gold : Int
) : InventoryHolder {


    var listener : HeroListener? = null

    interface HeroListener {
        fun heroGainedGold(hero: Hero, amountGained : Int)
        fun heroLostGold(hero: Hero, amountLost : Int)
        fun updateHeroName(hero: Hero)
        fun updateHeroClass(hero: Hero)
        fun heroLostHitPoints(hero: Hero, amountLost : Int)
        fun heroRecoveredHitPoints(hero: Hero, amountRecovered : Int)
        fun heroGainedExperiencePoints(hero: Hero, amountGained: Int)
        fun heroLostExperiencePoints(hero: Hero, amountLost: Int)
        fun heroUpgradedLevel(hero: Hero, amountGained: Int)
        fun heroDowngradedLevel(hero: Hero, amountLost: Int)
        fun heroReceivedQuest(hero: Hero, quest: Quest)
        fun heroCompletedQuest(hero: Hero, quest: Quest)


    }

    var name : String = name
        set(value) {
            field = value
            listener?.updateHeroName(this)
        }
    var characterClass : String = characterClass
        set(value) {
            field = value
            listener?.updateHeroClass(this)
        }

    var hitPoints : Int = hitPoints
        set(value) {
            val diff = value - field
            field = value
            if (diff > 0)
                listener?.heroRecoveredHitPoints(this, diff)
            else
                listener?.heroLostHitPoints(this, diff)
        }
    var experiencePoints : Int = experiencePoints
        set(value) {
            val diff = value - field
            field = value
            if (diff > 0)
                listener?.heroGainedExperiencePoints(this, diff)
            else
                listener?.heroLostExperiencePoints(this, diff)
        }
    var level : Int = level
        set(value) {
            val diff = value - field
            field = value
            if (diff > 0)
                listener?.heroUpgradedLevel(this, diff)
            else
                listener?.heroDowngradedLevel(this, diff)
        }

    var gold : Int = gold
        set(value) {
            val diff = value - field
            field = value
            if (diff > 0)
                listener?.heroGainedGold(this, diff)
            else
                listener?.heroLostGold(this, diff)
        }


    override val inventory = Inventory(this)

    private var __questStatus = mutableMapOf<Quest, Boolean>()
    val questStatus : Map<Quest, Boolean>
        get() = __questStatus

    fun assignQuest(quest : Quest){
        if (quest !in questStatus){
            __questStatus[quest] = false
            listener?.heroReceivedQuest(this, quest)
        }
    }
    fun hasQuestNotCompleted(quest: Quest): Boolean {
        return questStatus.filter { (quest1, isDone) -> quest1 == quest && !isDone }.isNotEmpty()
    }

    fun markQuestAsComplete(quest: Quest) {
        if (quest in questStatus){
            __questStatus[quest] = true
            listener?.heroCompletedQuest(this, quest)
        }

    }

    var currentWeapon : ItemInstance? = null

    fun equipWeapon(weapon: Item) {
        println("equipping")
        currentWeapon = inventory.weapons.firstOrNull { itemInstance -> itemInstance.item == weapon }
    }

    fun unequipWeapon() {
        println("unequipping")
        currentWeapon = null
    }
}
