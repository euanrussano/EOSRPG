package com.sophia.eosrpg.model.item

import com.sophia.eosrpg.model.entity.Entity

interface BaseItem {
    val name : String
    val price : Int

    fun performAction(actor : Entity, target : Entity)
}
