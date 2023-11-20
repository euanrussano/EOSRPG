package com.sophia.eosrpg.model.quest

class Quest(
    val name : String,
    val description : String,
    val task : QuestTask,
    val reward : QuestReward
) {
}
