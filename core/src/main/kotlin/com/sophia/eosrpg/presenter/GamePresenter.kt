package com.sophia.eosrpg.presenter

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.math.MathUtils
import com.sophia.eosrpg.model.*
import com.sophia.eosrpg.model.item.*
import com.sophia.eosrpg.model.monster.MonsterFactory
import com.sophia.eosrpg.model.monster.MonsterInstance
import com.sophia.eosrpg.model.monster.MonsterInstanceFactory
import com.sophia.eosrpg.model.monster.MonsterRepository
import com.sophia.eosrpg.model.quest.Quest
import com.sophia.eosrpg.model.quest.QuestFactory
import com.sophia.eosrpg.model.quest.QuestRepository
import com.sophia.eosrpg.model.trader.Trader
import com.sophia.eosrpg.model.trader.TraderFactory
import com.sophia.eosrpg.model.trader.TraderRepository
import com.sophia.eosrpg.screen.GameScreen

class GamePresenter(val gameScreen: GameScreen) : MonsterInstance.MonsterListener, Telegraph { // Trader.TraderListener,

    val heroListener = HeroListenerPresenter(gameScreen)

    val itemRepository = ItemRepository()
    val itemFactory = ItemFactory(itemRepository)
    val itemInstanceFactory = ItemInstanceFactory(itemRepository)

    val questRepository = QuestRepository()
    val questFactory = QuestFactory(questRepository, itemRepository)

    val traderRepository = TraderRepository()
    val traderFactory = TraderFactory(traderRepository, itemInstanceFactory)

    private val monsterRepository = MonsterRepository()
    private val monsterFactory = MonsterFactory(monsterRepository, itemRepository)
    private val monsterInstanceFactory = MonsterInstanceFactory(monsterRepository, itemInstanceFactory)

    var currentHero : Hero? = null
        set(value) {
            field?.listener = null
            field = value
            value?.listener = heroListener
            value?.let { gameScreen.updateHero(it) }
        }

    val worldFactory = WorldFactory(questRepository, traderRepository)
    var currentWorld = worldFactory.createWorld()

    var currentLocation = currentWorld.locationAt(0, 0)!!
        set(value) {
            field = value
            gameScreen.updateLocation(currentLocation)
            completeQuestsAtLocation()
            giveHeroQuestsAtLocation()
            getMonsterInstanceAtLocation();
            currentTrader = field.traderHere
        }


    val hasLocationNorth : Boolean
        get() = currentWorld.locationAt(currentLocation.x, currentLocation.y + 1) != null
    val hasLocationSouth : Boolean
        get() = currentWorld.locationAt(currentLocation.x, currentLocation.y - 1) != null
    val hasLocationWest : Boolean
        get() = currentWorld.locationAt(currentLocation.x-1, currentLocation.y) != null
    val hasLocationEast : Boolean
        get() = currentWorld.locationAt(currentLocation.x+1, currentLocation.y) != null

    var currentMonsterInstance : MonsterInstance? = null
        set(value) {
            value?.listener = null
            field = value
            field?.let {
                it.listener = this@GamePresenter
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
//            field?.listener = null
            field = value
//            value?.listener = this
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
            Messages.EntityWasKilledEvent.code
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
             val inventory = InventoryHolderComponent.get(this)
            inventory.addItemInstanceToInventory(
                itemInstanceFactory.createItemInstance("Pointy Stick"),
            )
            inventory.addItemInstanceToInventory(
                itemInstanceFactory.createItemInstance("Granola Bar"),
            )
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
        currentWorld.locationAt(currentLocation.x, currentLocation.y+1)?.let {
            currentLocation = it
        }
    }

    fun moveHeroSouth() {
        currentWorld.locationAt(currentLocation.x, currentLocation.y-1)?.let {
            currentLocation = it
        }

    }

    fun moveHeroWest() {
        currentWorld.locationAt(currentLocation.x-1, currentLocation.y)?.let {
            currentLocation = it
        }

    }

    fun moveHeroEast() {
        currentWorld.locationAt(currentLocation.x+1, currentLocation.y)?.let {
            currentLocation = it
        }
    }

    override fun updateMonsterInstance(monsterInstance: MonsterInstance) {
        gameScreen.updateMonsterInstance(monsterInstance)
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
        if (!LivingEntityComponent.get(monsterInstance).isDead){
            monsterInstance.useCurrentWeaponOn(hero)
//            val damageToHero = MathUtils.random(monsterInstance.monster.minimumDamage, monsterInstance.monster.maximumDamage)
//            if (damageToHero == 0){
//                gameScreen.raiseMessage("Monster attacks, but misses you.")
//            } else {
//                LivingEntityComponent.get(hero).takeDamage(damageToHero)
//
//            }
        }
//        val heroWeapon = hero.currentWeapon ?: return
//        heroWeapon.performAction(hero, )
//        val damageItemComponent = (heroWeapon.item as Item).get(DamageItemComponent::class) as DamageItemComponent
//        val damageToMonster = MathUtils.random(damageItemComponent.minimumDamage, damageItemComponent.maximumDamage)
//        if (damageToMonster == 0){
//            gameScreen.raiseMessage("You missed the ${monsterInstance.monster.name}")
//        } else {
//            monsterInstance.hitPoints -= damageToMonster
//            gameScreen.raiseMessage("You hit the ${monsterInstance.monster.name} for ${damageToMonster} points.")
//        }

//        if (monsterInstance.hitPoints <= 0){
//            gameScreen.raiseMessage("")
//            gameScreen.raiseMessage("You defeated the ${monsterInstance.monster.name}")
//            val xpPoints = monsterInstance.monster.rewardExperiencePoints
//            hero.experiencePoints += xpPoints
//            val gold = monsterInstance.monster.rewardGold
//            hero.gold += gold
//            for (itemInstance in monsterInstance.inventory.itemInstances) {
//                hero.inventory.addItemInstanceToInventory(itemInstance)
//            }
//            getMonsterInstanceAtLocation()
//        }
//        else {
//
//        }

//        if (hero.hitPoints <= 0){
//            gameScreen.raiseMessage("")
//            gameScreen.raiseMessage("You fainted!")
//            currentLocation = currentWorld.locationAt(0, -1)!!
//            hero.hitPoints = hero.level*10
//
//        }

    }

    fun heroSellItemInstance(itemInstance: ItemInstance) {
        val hero = currentHero ?: return
        val trader = currentTrader ?: return
        val heroInventory = InventoryHolderComponent.get(hero)
        val traderInventory = InventoryHolderComponent.get(trader)

        heroInventory.removeItemInstanceToInventory(itemInstance)
        traderInventory.addItemInstanceToInventory(itemInstance)
        hero.gold += itemInstance.item.price
    }

    fun heroBuyItemInstance(itemInstance: ItemInstance) {
        val hero = currentHero ?: return
        val trader = currentTrader ?: return
        if (hero.gold < itemInstance.item.price) return

        val heroInventory = InventoryHolderComponent.get(hero)
        val traderInventory = InventoryHolderComponent.get(trader)

        traderInventory.removeItemInstanceToInventory(itemInstance)
        heroInventory.addItemInstanceToInventory(itemInstance)
        hero.gold -= itemInstance.item.price

    }


    class HeroListenerPresenter(val gameScreen: GameScreen) : Hero.HeroListener {
//        override fun itemInstanceAddedToInventory(hero: Hero, itemInstance: ItemInstance) {
//            gameScreen.updateHeroInventory(hero.inventory)
//            gameScreen.updateHeroWeapons(hero.weapons)
//            gameScreen.updateHeroCurrentWeapon(hero.currentWeapon)
//            gameScreen.raiseMessage("You received a ${itemInstance.item.name}")
//        }
//
//        override fun itemInstanceRemovedFromInventory(hero: Hero, itemInstance: ItemInstance) {
//            gameScreen.updateHeroInventory(hero.inventory)
//            gameScreen.raiseMessage("${itemInstance.item.name} removed from inventory")
//        }

        override fun heroGainedGold(hero: Hero, amountGained: Int) {
            gameScreen.updateHeroGold(hero.gold)
            gameScreen.raiseMessage("You received ${amountGained} gold.")
        }

        override fun heroLostGold(hero: Hero, amountLost: Int) {
            gameScreen.updateHeroGold(hero.gold)
            gameScreen.raiseMessage("You spent/lost ${amountLost} gold.")
        }

        override fun updateHeroName(hero: Hero) {
            TODO("Not yet implemented")
        }

        override fun updateHeroClass(hero: Hero) {
            TODO("Not yet implemented")
        }

//        override fun heroRecoveredHitPoints(hero: Hero, amountRecovered: Int) {
//            gameScreen.updateHeroHitPoints(hero.hitPoints)
//            gameScreen.raiseMessage("You recovered ${amountRecovered} hit points.")
//        }

        override fun heroGainedExperiencePoints(hero: Hero, amountGained: Int) {
            gameScreen.updateHeroXP(hero.experiencePoints)
            gameScreen.raiseMessage("You received ${amountGained} experience points.")
        }

        override fun heroLostExperiencePoints(hero: Hero, amountLost: Int) {
            gameScreen.updateHeroXP(hero.experiencePoints)
            gameScreen.raiseMessage("You lost ${amountLost} experience points.")
        }

        override fun heroUpgradedLevel(hero: Hero, amountGained: Int) {
            gameScreen.updateHeroLevel(hero.level)
            gameScreen.raiseMessage("You advanced to ${hero.level} level.")
        }

        override fun heroDowngradedLevel(hero: Hero, amountLost: Int) {
            gameScreen.updateHeroLevel(hero.level)
            gameScreen.raiseMessage("You downgraded to ${hero.level} level.")
        }

        override fun heroReceivedQuest(hero: Hero, quest: Quest) {
            gameScreen.updateHeroQuests(hero.questStatus)
            gameScreen.raiseMessage("")
            gameScreen.raiseMessage("You receive the ${quest.name} quest")
            gameScreen.raiseMessage(quest.description)
            gameScreen.raiseMessage("Return with:\n")
            gameScreen.raiseMessage(quest.task.toString())
            gameScreen.raiseMessage("And you will receive:")
            gameScreen.raiseMessage(quest.reward.toString())
        }

        override fun heroCompletedQuest(hero: Hero, quest: Quest) {
            gameScreen.updateHeroQuests(hero.questStatus)
            gameScreen.raiseMessage("")
            gameScreen.raiseMessage("You have completed the ${quest.name} quest")
        }

//        override fun heroLostHitPoints(hero: Hero, amountLost: Int) {
//            gameScreen.updateHeroHitPoints(hero.hitPoints)
//            gameScreen.raiseMessage("You were hit for ${amountLost} hit points.")
//        }

    }

//    override fun itemInstanceAddedToInventory(trader: Trader, itemInstance: ItemInstance) {
//        gameScreen.updateTrader(trader)
//    }
//
//    override fun itemInstanceRemovedFromInventory(trader: Trader, itemInstance: ItemInstance) {
//        gameScreen.updateTrader(trader)
//    }

    override fun handleMessage(msg: Telegram): Boolean {
        when(msg.message){
            Messages.ItemInstanceAddedEvent.code -> return onItemInstanceAddedEvent(msg.extraInfo as Messages.ItemInstanceAddedEvent)
            Messages.ItemInstanceRemovedEvent.code -> return onItemInstanceRemovedEvent(msg.extraInfo as Messages.ItemInstanceRemovedEvent)
            Messages.EntityAttackEvent.code -> return onEntityAttackedEvent(msg.extraInfo as Messages.EntityAttackEvent)
            Messages.EntityWasFullyHealed.code -> return onEntityWasFullyHealed(msg.extraInfo as Messages.EntityWasFullyHealed)
            Messages.EntityWasHealed.code -> return onEntityWasHealed(msg.extraInfo as Messages.EntityWasHealed)
            Messages.EntityWasHitEvent.code -> return onEntityWasHit(msg.extraInfo as Messages.EntityWasHitEvent)
            Messages.EntityWasKilledEvent.code -> return onEntityWasKilledEvent(msg.extraInfo as Messages.EntityWasKilledEvent)
        }
        return false
    }

    private fun onEntityWasKilledEvent(event: Messages.EntityWasKilledEvent): Boolean {
        if (event.owner == currentHero){
            gameScreen.raiseMessage("")
            gameScreen.raiseMessage("You fainted!")
            currentLocation = currentWorld.locationAt(0, -1)!!
            LivingEntityComponent.get(event.owner).fullyHeal()
            return true
        } else if (event.owner is MonsterInstance){
            val monsterInstance = event.owner
            val hero = currentHero!!
            gameScreen.raiseMessage("")
            gameScreen.raiseMessage("You defeated the ${monsterInstance.monster.name}")
            val xpPoints = monsterInstance.monster.rewardExperiencePoints
            hero.experiencePoints += xpPoints
            val gold = monsterInstance.monster.rewardGold
            hero.gold += gold
            val heroInventory = InventoryHolderComponent.get(hero)
            val monsterInventory = InventoryHolderComponent.get(monsterInstance)
            for (itemInstance in monsterInventory.itemInstances) {
                heroInventory.addItemInstanceToInventory(itemInstance)
            }
            getMonsterInstanceAtLocation()
        }
        return false
    }

    private fun onEntityWasHit(event: Messages.EntityWasHitEvent): Boolean {
        if (event.owner == currentHero){
            gameScreen.updateHeroHitPoints(LivingEntityComponent.get(event.owner).hitPoints)
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
            gameScreen.updateHeroHitPoints(LivingEntityComponent.get(event.owner).hitPoints)
            gameScreen.raiseMessage("You recovered ${event.amount} hit points.")
            return true
        }
        return false
    }

    private fun onEntityWasFullyHealed(event: Messages.EntityWasFullyHealed): Boolean {
        //TODO("Not yet implemented")
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

            gameScreen.updateHeroInventory(InventoryHolderComponent.get(owner).itemInstances)
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

            val heroInventory = InventoryHolderComponent.get(owner)

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


}
