package com.sophia.eosrpg.model.item

import com.sophia.eosrpg.model.entity.Entity

class ItemInstance(
    val item : Item
) {
    fun performAction(hero: Entity, monsterInstance: Entity) {
        item.performAction(hero, monsterInstance)
    }
}
