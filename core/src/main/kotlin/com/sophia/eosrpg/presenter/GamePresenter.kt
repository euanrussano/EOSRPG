package com.sophia.eosrpg.presenter

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

class GamePresenter(val gameScreen: GameScreen) : MonsterInstance.MonsterListener {

    val heroListener = HeroListenerPresenter(gameScreen)

    val itemRepository = ItemRepository()
    val itemFactory = ItemFactory(itemRepository)
    val itemInstanceFactory = ItemInstanceFactory(itemRepository)

    val questRepository = QuestRepository()
    val questFactory = QuestFactory(questRepository, itemRepository)

    val traderRepository = TraderRepository()
    val traderFactory = TraderFactory(traderRepository, itemInstanceFactory)

    private val monsterRepository = MonsterRepository()
    private val monsterFactory = MonsterFactory(monsterRepository)
    private val monsterInstanceFactory = MonsterInstanceFactory(monsterRepository)

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
            field = value
            if (field != null)
                gameScreen.updateTrader(field!!)
            else
                gameScreen.removeTrader()
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
            addItemInstanceToInventory(
                itemInstanceFactory.createItemInstance("Pointy Stick"),
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
        val heroWeapon = hero.currentWeapon ?: return

        val damageItem = (heroWeapon.item as CompositeItem).get(DamageItem::class) as DamageItem
        val damageToMonster = MathUtils.random(damageItem.minimumDamage, damageItem.maximumDamage)
        if (damageToMonster == 0){
            gameScreen.raiseMessage("You missed the ${monsterInstance.monster.name}")
        } else {
            monsterInstance.hitPoints -= damageToMonster
            gameScreen.raiseMessage("You hit the ${monsterInstance.monster.name} for ${damageToMonster} points.")
        }

        if (monsterInstance.hitPoints <= 0){
            gameScreen.raiseMessage("")
            gameScreen.raiseMessage("You defeated the ${monsterInstance.monster.name}")
            val xpPoints = monsterInstance.monster.rewardExperiencePoints
            hero.experiencePoints += xpPoints
            //gameScreen.raiseMessage("You received ${xpPoints} experience points.")
            val gold = monsterInstance.monster.rewardGold
            hero.gold += gold
            for ((itemName, qty) in monsterInstance.inventory) {
                for (i in 0 until qty){
                    val itemInstance = itemInstanceFactory.createItemInstance(itemName)
                    hero.addItemInstanceToInventory(itemInstance)
                }
            }
            getMonsterInstanceAtLocation()
        }
        else {
            val damageToHero = MathUtils.random(monsterInstance.monster.minimumDamage, monsterInstance.monster.maximumDamage)
            if (damageToHero == 0){
                gameScreen.raiseMessage("Monster attacks, but misses you.")
            } else {
                hero.hitPoints -= damageToHero

            }
        }

        if (hero.hitPoints <= 0){
            gameScreen.raiseMessage("")
            gameScreen.raiseMessage("You fainted!")
            currentLocation = currentWorld.locationAt(0, -1)!!
            hero.hitPoints = hero.level*10

        }

    }


    class HeroListenerPresenter(val gameScreen: GameScreen) : Hero.HeroListener {
        override fun itemInstanceAddedToInventory(hero: Hero, itemInstance: ItemInstance) {
            gameScreen.updateHeroInventory(hero.inventory)
            gameScreen.updateHeroWeapons(hero.weapons)
            gameScreen.updateHeroCurrentWeapon(hero.currentWeapon)
            gameScreen.raiseMessage("You received a ${itemInstance.item.name}")
        }

        override fun itemInstanceRemovedFromInventory(hero: Hero, itemInstance: ItemInstance) {
            gameScreen.updateHeroInventory(hero.inventory)
            gameScreen.raiseMessage("${itemInstance.item.name} removed from inventory")
        }

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

        override fun heroRecoveredHitPoints(hero: Hero, amountRecovered: Int) {
            gameScreen.updateHeroHitPoints(hero.hitPoints)
            gameScreen.raiseMessage("You recovered ${amountRecovered} hit points.")
        }

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

        override fun heroLostHitPoints(hero: Hero, amountLost: Int) {
            gameScreen.updateHeroHitPoints(hero.hitPoints)
            gameScreen.raiseMessage("You were hit for ${amountLost} hit points.")
        }

    }


}
