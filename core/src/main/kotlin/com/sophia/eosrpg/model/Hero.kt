package com.sophia.eosrpg.model

import com.badlogic.gdx.ai.msg.MessageManager
import com.sophia.eosrpg.model.item.ItemInstance
import com.sophia.eosrpg.model.item.Item
import com.sophia.eosrpg.model.monster.MonsterInstance
import com.sophia.eosrpg.model.quest.Quest
import com.sophia.eosrpg.model.entity.Entity
import com.sophia.eosrpg.model.recipe.Recipe

class Hero(
    name : String,
    characterClass : String,
    hitPoints : Int,
    experiencePoints : Int,
    level : Int,
    gold : Int
) :  Entity(10) {

//    init {
//        add(LivingEntityComponent(this, 10))
//        add(InventoryHolderComponent(this))
//    }

    var name : String = name
        set(value) {
            field = value
        }
    var characterClass : String = characterClass
        set(value) {
            field = value
        }

    var __experiencePoints : Int = experiencePoints
    val experiencePoints : Int
        get() = __experiencePoints

    fun increaseXP(amount : Int){
        __experiencePoints += amount
        val event = Messages.HeroGainedXP(this, amount)
        val code = Messages.HeroGainedXP.code
        MessageManager.getInstance().dispatchMessage(code, event)
    }

    private var __level : Int = level
    val level : Int; get() = __level
    fun raiseLevel(){
        __level += 1
        val event = Messages.HeroGainedLevel(this)
        val code = Messages.HeroGainedLevel.code
        MessageManager.getInstance().dispatchMessage(code, event)
    }

    private var __gold : Int = gold
    val gold : Int; get() = __gold
    fun receiveGold(amount : Int){
        __gold += amount
        val event = Messages.HeroReceivedGoldEvent(this, amount)
        val code = Messages.HeroReceivedGoldEvent.code
        MessageManager.getInstance().dispatchMessage(code, event)
    }

    fun spendGold(amount : Int){
        __gold -= amount
        val event = Messages.HeroSpentGoldEvent(this, amount)
        val code = Messages.HeroSpentGoldEvent.code
        MessageManager.getInstance().dispatchMessage(code, event)
    }

    private var __questStatus = mutableMapOf<Quest, Boolean>()
    val questStatus : Map<Quest, Boolean>
        get() = __questStatus

    fun assignQuest(quest : Quest){
        if (quest !in questStatus){
            __questStatus[quest] = false
            val event = Messages.HeroReceivedQuestEvent(this, quest)
            val code = Messages.HeroReceivedQuestEvent.code
            MessageManager.getInstance().dispatchMessage(code, event)
        }
    }
    fun hasQuestNotCompleted(quest: Quest): Boolean {
        return questStatus.filter { (quest1, isDone) -> quest1 == quest && !isDone }.isNotEmpty()
    }

    fun markQuestAsComplete(quest: Quest) {
        if (quest in questStatus){
            __questStatus[quest] = true
            val event = Messages.HeroCompletedQuestEvent(this, quest)
            val code = Messages.HeroCompletedQuestEvent.code
            MessageManager.getInstance().dispatchMessage(code, event)
        }

    }

    var currentWeapon : ItemInstance? = null

    fun equipWeapon(weapon: Item) {
        println("equipping")
        val inventory = this.inventory
        currentWeapon = inventory.weapons.firstOrNull { itemInstance -> itemInstance.item == weapon }
    }

    fun unequipWeapon() {
        println("unequipping")
        currentWeapon = null
    }

    fun useCurrentWeaponOn(monsterInstance: MonsterInstance) {
        currentWeapon?.performAction(this, monsterInstance)
    }

    fun consumeItem(itemName: String) {
        val inventory = this.inventory

        val itemInstance = inventory.itemInstances.firstOrNull { itemInstance -> itemInstance.item.name == itemName } ?: return
        itemInstance.performAction(this, this)
        inventory.removeItemInstanceToInventory(itemInstance)
    }

    private val __recipes = mutableListOf<Recipe>()
    val recipes;get() = __recipes
    fun learnRecipe(recipe: Recipe){
        if (recipe !in __recipes){
            __recipes.add(recipe)
            val event = Messages.HeroLearntRecipeEvent(this, recipe)
            val code = Messages.HeroLearntRecipeEvent.code
            MessageManager.getInstance().dispatchMessage(code, event)
        }
    }
}
