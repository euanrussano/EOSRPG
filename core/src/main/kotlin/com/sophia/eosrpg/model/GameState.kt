package com.sophia.eosrpg.model

import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.math.MathUtils
import com.sophia.eosrpg.model.location.Location
import com.sophia.eosrpg.model.location.LocationRepository
import com.sophia.eosrpg.model.monster.MonsterInstance
import com.sophia.eosrpg.model.monster.MonsterInstanceFactory
import com.sophia.eosrpg.model.trader.Trader

class GameState(
    val locationRepository : LocationRepository,
    val monsterInstanceFactory: MonsterInstanceFactory
){

    var currentHero : Hero? = null
        set(value) {
            field = value
            val event = Messages.CurrentHeroChangedEvent(value)
            val code = Messages.CurrentHeroChangedEvent.code
            MessageManager.getInstance().dispatchMessage(code, event)
        }

    var currentLocation : Location = locationRepository.locationAt(0, 0)!!
        set(value) {
            field = value
            val event = Messages.CurrentLocationChangedEvent(value)
            val code = Messages.CurrentLocationChangedEvent.code
            MessageManager.getInstance().dispatchMessage(code, event)

            completeQuestsAtLocation()
            giveHeroQuestsAtLocation()
            getMonsterInstanceAtLocation();
            currentTrader = field?.traderHere
        }

    var currentMonsterInstance : MonsterInstance? = null
        set(value) {
            field = value
            val event = Messages.CurrentMonsterInstanceChangedEvent(value)
            val code = Messages.CurrentMonsterInstanceChangedEvent.code
            MessageManager.getInstance().dispatchMessage(code, event)


        }

    var currentTrader : Trader? = null
        set(value) {
            field = value
            val event = Messages.CurrentTraderChangedEvent(value)
            val code = Messages.CurrentTraderChangedEvent.code
            MessageManager.getInstance().dispatchMessage(code, event)

        }

    private fun completeQuestsAtLocation() {
        val hero = currentHero ?: return
        val location = currentLocation ?: return

        for (quest in location.questsAvailableHere) {
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
        val location = currentLocation ?: return

        for (quest in location.questsAvailableHere){
            hero.assignQuest(quest)
        }
    }

    fun getMonsterInstanceAtLocation() {
        val location = currentLocation ?: return

        if (location.monsterProbability.isEmpty()) {
            currentMonsterInstance = null
            return
        }
        val totalChance = location.monsterProbability.values.sum()
        val randomNumber = MathUtils.random(1, totalChance)

        var runningTotal = 0
        for ((monsterName, prob) in location.monsterProbability) {
            runningTotal += prob
            if (randomNumber <= runningTotal){
                currentMonsterInstance = monsterInstanceFactory.createMonsterInstance(monsterName)
                return

            }
        }

        currentMonsterInstance = monsterInstanceFactory.createMonsterInstance(location.monsterProbability.keys.last())

    }

    fun moveHeroNorth() {
        val location = currentLocation?: return
        locationRepository.locationAt(location.x, location.y+1)?.let {
            currentLocation = it
        }
    }

    fun moveHeroSouth() {
        val location = currentLocation?: return
        locationRepository.locationAt(location.x, location.y-1)?.let {
            currentLocation = it
        }

    }

    fun moveHeroWest() {
        val location = currentLocation?: return
        locationRepository.locationAt(location.x-1, location.y)?.let {
            currentLocation = it
        }

    }

    fun moveHeroEast() {
        val location = currentLocation?: return
        locationRepository.locationAt(location.x+1, location.y)?.let {
            currentLocation = it
        }
    }

}
