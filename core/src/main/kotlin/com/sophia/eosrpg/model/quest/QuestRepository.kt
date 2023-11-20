package com.sophia.eosrpg.model.quest

class QuestRepository {

    val quests = mutableListOf<Quest>()

    fun save(quest: Quest) {
        quests.add(quest)
    }

    fun findByName(name: String): Quest {
        return quests.first { quest -> quest.name == name }
    }
}
