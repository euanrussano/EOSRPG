package com.sophia.eosrpg.model.quest

import com.sophia.eosrpg.model.Hero

interface QuestTask {
    fun canPerform(hero: Hero): Boolean
    fun perform(hero: Hero)


}
