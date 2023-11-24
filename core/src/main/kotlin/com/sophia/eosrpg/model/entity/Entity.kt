package com.sophia.eosrpg.model.entity

import com.sophia.eosrpg.model.skill.Skill

open class Entity(
    maximumHitPoints : Int
) {

    val inventory = Inventory(this)
    val health  = Health(this, maximumHitPoints)
    val ST = 10
    val DX = 10

    val skills = mutableMapOf<Skill, Int>()

    val GdP = Dice(1, -2)
    val GeB = Dice(1)

}
