package com.sophia.eosrpg.model.quest

import com.sophia.eosrpg.model.Hero

interface QuestReward {
    fun giveTo(hero: Hero)

}
