package com.sophia.eosrpg.presenter

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.math.MathUtils
import com.sophia.eosrpg.model.*
import com.sophia.eosrpg.model.item.*
import com.sophia.eosrpg.model.item.factory.ItemFactory
import com.sophia.eosrpg.model.item.factory.XMLItemFactory
import com.sophia.eosrpg.model.location.LocationRepository
import com.sophia.eosrpg.model.location.XMLLocationFactory
import com.sophia.eosrpg.model.monster.*
import com.sophia.eosrpg.model.quest.QuestRepository
import com.sophia.eosrpg.model.quest.XMLQuestFactory
import com.sophia.eosrpg.model.recipe.*
import com.sophia.eosrpg.model.trader.Trader
import com.sophia.eosrpg.model.trader.TraderRepository
import com.sophia.eosrpg.model.trader.XMLTraderFactory
import com.sophia.eosrpg.screen.GameScreen

class GamePresenter(val gameScreen: GameScreen) : Telegraph { // Trader.TraderListener,

//    val heroListener = HeroListenerPresenter(gameScreen)

    val itemRepository = ItemRepository()
    //val itemFactory = ItemFactory(itemRepository)
    val itemFactory : ItemFactory = XMLItemFactory(itemRepository)
    val itemInstanceFactory = ItemInstanceFactory(itemRepository)

    val recipeRepository = RecipeRepository()
    val recipeFactory = XMLRecipeFactory(recipeRepository, itemRepository)
    val recipeService = RecipeService(recipeRepository, itemInstanceFactory)

    val questRepository = QuestRepository()
    val questFactory = XMLQuestFactory(questRepository, itemRepository)

    val traderRepository = TraderRepository()
    val traderFactory = XMLTraderFactory(traderRepository, itemInstanceFactory)

    private val monsterRepository = MonsterRepository()
    private val monsterFactory = XMLMonsterFactory(monsterRepository, itemRepository)
    private val monsterInstanceFactory = MonsterInstanceFactory(monsterRepository, itemInstanceFactory)

    val locationRepository = LocationRepository()
    val locationFactory = XMLLocationFactory(locationRepository, questRepository, traderRepository)


    var currentHero : Hero? = null
        set(value) {
            field = value
            value?.let { gameScreen.updateHero(it) }
        }

    var currentLocation = locationRepository.locationAt(0, 0)!!
        set(value) {
            field = value
            gameScreen.updateLocation(currentLocation)
            completeQuestsAtLocation()
            giveHeroQuestsAtLocation()
            getMonsterInstanceAtLocation();
            currentTrader = field.traderHere
        }


    val hasLocationNorth : Boolean
        get() = locationRepository.locationAt(currentLocation.x, currentLocation.y + 1) != null
    val hasLocationSouth : Boolean
        get() = locationRepository.locationAt(currentLocation.x, currentLocation.y - 1) != null
    val hasLocationWest : Boolean
        get() = locationRepository.locationAt(currentLocation.x-1, currentLocation.y) != null
    val hasLocationEast : Boolean
        get() = locationRepository.locationAt(currentLocation.x+1, currentLocation.y) != null

    var currentMonsterInstance : MonsterInstance? = null
        set(value) {
            field = value
            field?.let {
                gameScreen.updateMonsterInstance(it)
                gameScreen.raiseMessage("");
                gameScreen.raiseMessage("You see a ${it.monster.name} here!");
            }
            if (field == null){
                gameScreen.clearMonsterInstance()
            }
        }

    var currentTrader : Trader? = null
        set(value) {
            field = value
            if (field != null)
                gameScreen.updateTrader(field!!)
            else
                gameScreen.removeTrader()
        }

    init {
        MessageManager.getInstance().addListeners(this,
            Messages.ItemInstanceAddedEvent.code,
            Messages.ItemInstanceRemovedEvent.code,
            Messages.EntityAttackEvent.code,
            Messages.EntityWasFullyHealed.code,
            Messages.EntityWasHealed.code,
            Messages.EntityWasHitEvent.code,
            Messages.EntityWasKilledEvent.code,
            Messages.HeroLearntRecipeEvent.code,
            Messages.HeroReceivedGoldEvent.code,
            Messages.HeroSpentGoldEvent.code,
            Messages.HeroCompletedQuestEvent.code,
            Messages.HeroReceivedQuestEvent.code,
            Messages.HeroGainedXP.code
            )
    }


    fun initialize() {
        currentHero = Hero(
        "Scott",
        "Fighter",
        10,
        0,
        1,
        10_000
         ).apply {
             val inventory = this.inventory
            inventory.addItemInstanceToInventory(
                itemInstanceFactory.createItemInstance("Pointy Stick"),
            )
            inventory.addItemInstanceToInventory(
                itemInstanceFactory.createItemInstance("Granola Bar"),
            )
            inventory.addItemInstanceToInventory(
                itemInstanceFactory.createItemInstance("Oats"),
            )
            inventory.addItemInstanceToInventory(
                itemInstanceFactory.createItemInstance("Honey"),
            )
            inventory.addItemInstanceToInventory(
                itemInstanceFactory.createItemInstance("Raisins"),
            )

            learnRecipe(recipeRepository.findByName("Granola Bar"))
        }
        gameScreen.updateHero(currentHero!!)
        gameScreen.updateLocation(currentLocation)
    }

    private fun completeQuestsAtLocation() {
        val hero = currentHero ?: return
        for (quest in currentLocation.questsAvailableHere) {
            if (hero.hasQuestNotCompleted(quest)){
                if (quest.task.canPerform(hero)){
                    quest.task.perform(hero)
                    quest.reward.giveTo(hero)

                    hero.markQuestAsComplete(quest)
                }
            }
        }
    }

    private fun giveHeroQuestsAtLocation() {
        val hero = currentHero?: return
        for (quest in currentLocation.questsAvailableHere){
            hero.assignQuest(quest)
        }
    }

    private fun getMonsterInstanceAtLocation() {
        if (currentLocation.monsterProbability.isEmpty()) {
            currentMonsterInstance = null
            return
        }
        val totalChance = currentLocation.monsterProbability.values.sum()
        val randomNumber = MathUtils.random(1, totalChance)

        var runningTotal = 0
        for ((monsterName, prob) in currentLocation.monsterProbability) {
            runningTotal += prob
            if (randomNumber <= runningTotal){
                currentMonsterInstance = monsterInstanceFactory.createMonsterInstance(monsterName)
                return

            }
        }

        currentMonsterInstance = monsterInstanceFactory.createMonsterInstance(currentLocation.monsterProbability.keys.last())

    }

    fun moveHeroNorth() {
        locationRepository.locationAt(currentLocation.x, currentLocation.y+1)?.let {
            currentLocation = it
        }
    }

    fun moveHeroSouth() {
        locationRepository.locationAt(currentLocation.x, currentLocation.y-1)?.let {
            currentLocation = it
        }

    }

    fun moveHeroWest() {
        locationRepository.locationAt(currentLocation.x-1, currentLocation.y)?.let {
            currentLocation = it
        }

    }

    fun moveHeroEast() {
        locationRepository.locationAt(currentLocation.x+1, currentLocation.y)?.let {
            currentLocation = it
        }
    }

    fun changeHeroCurrentWeapon(weaponName: String?) {
        val hero = currentHero?: return
        if (weaponName != null){
            val weapon = itemRepository.findByName(weaponName)
            hero.equipWeapon(weapon)
        } else {
            hero.unequipWeapon()
        }
    }

    fun attackCurrentMonster() {
        val hero = currentHero ?: return
        val monsterInstance = currentMonsterInstance?: return
        if (hero.currentWeapon == null){
            gameScreen.raiseMessage("You must select a weapon to attack.")
            return
        }
        hero.useCurrentWeaponOn(monsterInstance)
        if (!monsterInstance.health.isDead){
            monsterInstance.useCurrentWeaponOn(hero)
        }

    }

    fun heroSellItemInstance(itemInstance: ItemInstance) {
        val hero = currentHero ?: return
        val trader = currentTrader ?: return
        val heroInventory = hero.inventory
        val traderInventory = trader.inventory

        heroInventory.removeItemInstanceToInventory(itemInstance)
        traderInventory.addItemInstanceToInventory(itemInstance)
        hero.receiveGold(itemInstance.item.price)
    }

    fun heroBuyItemInstance(itemInstance: ItemInstance) {
        val hero = currentHero ?: return
        val trader = currentTrader ?: return
        if (hero.gold < itemInstance.item.price) return

        val heroInventory = hero.inventory
        val traderInventory = trader.inventory

        traderInventory.removeItemInstanceToInventory(itemInstance)
        heroInventory.addItemInstanceToInventory(itemInstance)
        hero.spendGold(itemInstance.item.price)

    }

    override fun handleMessage(msg: Telegram): Boolean {
        when(msg.message){
            Messages.ItemInstanceAddedEvent.code -> return onItemInstanceAddedEvent(msg.extraInfo as Messages.ItemInstanceAddedEvent)
            Messages.ItemInstanceRemovedEvent.code -> return onItemInstanceRemovedEvent(msg.extraInfo as Messages.ItemInstanceRemovedEvent)
            Messages.EntityAttackEvent.code -> return onEntityAttackedEvent(msg.extraInfo as Messages.EntityAttackEvent)
            Messages.EntityWasFullyHealed.code -> return onEntityWasFullyHealed(msg.extraInfo as Messages.EntityWasFullyHealed)
            Messages.EntityWasHealed.code -> return onEntityWasHealed(msg.extraInfo as Messages.EntityWasHealed)
            Messages.EntityWasHitEvent.code -> return onEntityWasHit(msg.extraInfo as Messages.EntityWasHitEvent)
            Messages.EntityWasKilledEvent.code -> return onEntityWasKilledEvent(msg.extraInfo as Messages.EntityWasKilledEvent)
            Messages.HeroLearntRecipeEvent.code -> return onHeroLearntRecipe(msg.extraInfo as Messages.HeroLearntRecipeEvent)
            Messages.HeroReceivedGoldEvent.code -> return onHeroReceivedGoldEvent(msg.extraInfo as Messages.HeroReceivedGoldEvent)
            Messages.HeroSpentGoldEvent.code -> return onHeroSpentGoldEvent(msg.extraInfo as Messages.HeroSpentGoldEvent)
            Messages.HeroCompletedQuestEvent.code -> return onHeroCompletedQuestEvent(msg.extraInfo as Messages.HeroCompletedQuestEvent)
            Messages.HeroReceivedQuestEvent.code -> return onHeroReceivedQuestEvent(msg.extraInfo as Messages.HeroReceivedQuestEvent)
            Messages.HeroGainedXP.code -> return onHeroGainedXP(msg.extraInfo as Messages.HeroGainedXP)
            else -> Gdx.app.error(this::class.simpleName, "No call for event ${msg.extraInfo}")
        }
        return false
    }

    private fun onHeroGainedXP(event: Messages.HeroGainedXP): Boolean {
        val hero = event.hero
        val amount = event.amount

        gameScreen.updateHeroXP(hero.experiencePoints)
        gameScreen.raiseMessage("You received ${amount} experience points.")

        return true
    }

    private fun onHeroReceivedGoldEvent(event: Messages.HeroReceivedGoldEvent): Boolean {
        val hero = event.hero
        val amount = event.amount

        gameScreen.updateHeroGold(hero.gold)
        gameScreen.raiseMessage("You received $amount gold.")
        return true
    }

    private fun onHeroSpentGoldEvent(event : Messages.HeroSpentGoldEvent) : Boolean{
        val hero = event.hero
        val gold = event.amount

        gameScreen.updateHeroGold(hero.gold)
        gameScreen.raiseMessage("You received ${gold} gold.")
        return true
    }

    private fun onHeroCompletedQuestEvent(event: Messages.HeroCompletedQuestEvent) : Boolean{
        val hero = event.hero
        val quest = event.quest

        gameScreen.updateHeroQuests(hero.questStatus)
        gameScreen.raiseMessage("")
        gameScreen.raiseMessage("You have completed the ${quest.name} quest")
        return true
    }

    private fun onHeroReceivedQuestEvent(event : Messages.HeroReceivedQuestEvent) : Boolean{
        val hero = event.hero
        val quest = event.quest

        gameScreen.updateHeroQuests(hero.questStatus)
        gameScreen.raiseMessage("")
        gameScreen.raiseMessage("You receive the ${quest.name} quest")
        gameScreen.raiseMessage(quest.description)
        gameScreen.raiseMessage(quest.task.toString())
        gameScreen.raiseMessage("And you will receive:")
        gameScreen.raiseMessage(quest.reward.toString())
        return true
    }

    private fun onHeroLearntRecipe(event: Messages.HeroLearntRecipeEvent): Boolean {
        val hero = event.hero
        if (hero != currentHero) return false
        gameScreen.updateHeroRecipes(hero.recipes)
        return true
    }

    private fun onEntityWasKilledEvent(event: Messages.EntityWasKilledEvent): Boolean {
        if (event.owner == currentHero){
            gameScreen.raiseMessage("")
            gameScreen.raiseMessage("You fainted!")
            currentLocation = locationRepository.locationAt(0, -1)!!
            event.owner.health.fullyHeal()
            return true
        } else if (event.owner is MonsterInstance){
            val monsterInstance = event.owner
            val hero = currentHero!!
            gameScreen.raiseMessage("")
            gameScreen.raiseMessage("You defeated the ${monsterInstance.monster.name}")
            val xpPoints = monsterInstance.monster.rewardExperiencePoints
            hero.increaseXP(xpPoints)
            val gold = monsterInstance.monster.rewardGold
            hero.receiveGold(gold)
            val heroInventory = hero.inventory
            val monsterInventory = monsterInstance.inventory
            for (itemInstance in monsterInventory.itemInstances) {
                heroInventory.addItemInstanceToInventory(itemInstance)
            }
            getMonsterInstanceAtLocation()
        }
        return false
    }

    private fun onEntityWasHit(event: Messages.EntityWasHitEvent): Boolean {
        if (event.owner == currentHero){
            gameScreen.updateHeroHitPoints(event.owner.health.hitPoints)
            gameScreen.raiseMessage("You were hit for ${event.damage} hit points.")
            return true
        } else if (event.owner == currentMonsterInstance){
            val monsterInstance = event.owner as MonsterInstance
            gameScreen.updateMonsterInstance(monsterInstance)
            gameScreen.raiseMessage("You hit the ${monsterInstance.monster.name} for ${event.damage} hit points.")
            return true
        }
        return false
    }

    private fun onEntityWasHealed(event: Messages.EntityWasHealed): Boolean {
        if (event.owner == currentHero){
            gameScreen.updateHeroHitPoints(event.owner.health.hitPoints)
            gameScreen.raiseMessage("You recovered ${event.amount} hit points.")
            return true
        }
        return false
    }

    private fun onEntityWasFullyHealed(event: Messages.EntityWasFullyHealed): Boolean {
        val owner = event.owner
        if (owner == currentHero){
            gameScreen.updateHero(owner as Hero)
            gameScreen.raiseMessage("You were fully healed!")
            return true
        }
        return false
    }

    private fun onEntityAttackedEvent(event: Messages.EntityAttackEvent): Boolean {
        //TODO("Not yet implemented")
        return false
    }

    private fun onItemInstanceRemovedEvent(event: Messages.ItemInstanceRemovedEvent): Boolean {
        val owner = event.owner
        if (owner is Hero) {
            val itemInstance = event.itemInstance

            gameScreen.updateHeroInventory(owner.inventory.itemInstances)
            gameScreen.raiseMessage("${itemInstance.item.name} removed from inventory")
            return true
        } else if (owner is Trader){
            gameScreen.updateTrader(owner)
            return true
        }
        return false
    }

    private fun onItemInstanceAddedEvent(event: Messages.ItemInstanceAddedEvent): Boolean {
        val owner = event.owner
        if (owner is Hero) {
            val itemInstance = event.itemInstance

            val heroInventory = owner.inventory

            gameScreen.updateHeroInventory(heroInventory.itemInstances)
            gameScreen.updateHeroWeapons(heroInventory.weapons)
            gameScreen.updateHeroCurrentWeapon(owner.currentWeapon)
            gameScreen.raiseMessage("You received a ${itemInstance.item.name}")
            return true
        } else if (owner is Trader){
            gameScreen.updateTrader(owner)
            return true
        }
        return false
    }

    fun heroConsumeItemInstance(itemName: String?) {
        val hero = currentHero ?: return
        itemName ?: return
        hero.consumeItem(itemName)


    }

    fun craftItem(recipe: Recipe) {
        val hero = currentHero ?: return
        if(recipeService.craftItem(hero, recipe)){
            for ((item, qty) in recipe.outputItems) {
                gameScreen.raiseMessage("You crafted $qty ${item.name}.")
            }

        }else {
            gameScreen.raiseMessage("You do not have the required ingredients:")
            for ((item, qty) in recipe.ingredients) {
                gameScreen.raiseMessage("$qty ${item.name}.")
            }
        }

    }


}
