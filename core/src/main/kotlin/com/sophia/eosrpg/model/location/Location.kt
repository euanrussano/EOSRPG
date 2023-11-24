package com.sophia.eosrpg.model.location

import com.sophia.eosrpg.model.quest.Quest
import com.sophia.eosrpg.model.trader.Trader

class Location(
    val x : Int,
    val y : Int,
    val name : String,
    val description : String,
    val questsAvailableHere : List<Quest> = listOf(),
    val monsterProbability : Map<String, Int> = mapOf(),
    val traderHere : Trader? = null
) {
}
