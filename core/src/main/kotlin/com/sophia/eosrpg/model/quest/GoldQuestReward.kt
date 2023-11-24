package com.sophia.eosrpg.model.quest

import com.sophia.eosrpg.model.Hero

class GoldQuestReward(
    val rewardGold : Int
) : QuestReward {

    override fun giveTo(hero: Hero) {
        hero.receiveGold(rewardGold)
    }

    override fun toString(): String {
        return "$rewardGold gold."
    }
}
