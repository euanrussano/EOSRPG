package com.sophia.eosrpg.model.quest

import com.sophia.eosrpg.model.Hero
import com.sophia.eosrpg.model.quest.QuestReward

class XPQuestReward(
    val experiencePoints : Int
) : QuestReward {

    override fun giveTo(hero: Hero) {
        hero.experiencePoints += experiencePoints
    }

    override fun toString(): String {
        return "$experiencePoints experience Points."
    }
}
