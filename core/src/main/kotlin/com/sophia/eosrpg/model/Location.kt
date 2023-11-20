package com.sophia.eosrpg.model

import com.sophia.eosrpg.model.quest.Quest

class Location(
    val x : Int,
    val y : Int,
    val name : String,
    val description : String,
    val questsAvailableHere : List<Quest> = listOf(),
    val monsterProbability : Map<String, Int> = mapOf()
) {
}
