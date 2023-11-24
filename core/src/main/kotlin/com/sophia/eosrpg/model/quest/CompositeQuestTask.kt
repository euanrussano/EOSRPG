package com.sophia.eosrpg.model.quest

import com.sophia.eosrpg.model.Hero

class CompositeQuestTask : QuestTask {

    val children = mutableListOf<QuestTask>()

    override fun canPerform(hero: Hero): Boolean {
        for (child in children) {
            if (!child.canPerform(hero)){
                return false
            }
        }
        return true
    }

    override fun perform(hero: Hero) {
        for (child in children) {
            child.perform(hero)
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
