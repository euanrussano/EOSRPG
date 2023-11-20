package com.sophia.eosrpg.model.quest

import com.sophia.eosrpg.model.Hero

class CompositeQuestReward(vararg rewards : QuestReward) : QuestReward {
    val children = mutableListOf<QuestReward>()

    init {
        children.addAll(rewards)
    }

    override fun giveTo(hero: Hero) {
        children.forEach {questReward ->
            questReward.giveTo(hero)
        }
    }

    override fun toString(): String {
        var stringOut = ""
        children.forEach { questReward ->
            stringOut = stringOut.plus(questReward.toString() + "\n")
        }
        return stringOut

    }
}
