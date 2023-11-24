package com.sophia.eosrpg.model.monster

import com.sophia.eosrpg.model.item.Item

class Monster(
    val name : String,
    val maximumHitPoints : Int,
    val rewardExperiencePoints : Int,
    val rewardGold : Int,
    val lootItems : Map<String, Int>,
    val weapon : Item
) {
}
