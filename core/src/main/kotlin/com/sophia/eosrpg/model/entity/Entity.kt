package com.sophia.eosrpg.model.entity

open class Entity(
    maximumHitPoints : Int
) {

    val inventory = Inventory(this)
    val health  = Health(this, maximumHitPoints)

}
