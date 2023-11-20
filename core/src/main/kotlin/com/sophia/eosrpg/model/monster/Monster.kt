package com.sophia.eosrpg.model.monster

class Monster(
    val name : String,
    val minimumDamage : Int,
    val maximumDamage : Int,
    val maximumHitPoints : Int,
    val rewardExperiencePoints : Int,
    val rewardGold : Int,
    val itemProbability : Map<String, Int>
) {
}
