package com.sophia.eosrpg.model.monster

import com.sophia.eosrpg.model.entity.Entity
import com.sophia.eosrpg.model.item.ItemInstance

class MonsterInstance(
    val monster : Monster,
) : Entity(monster.maximumHitPoints) {

    val weapon = ItemInstance(monster.weapon)

    fun useCurrentWeaponOn(entity: Entity) {
        weapon.performAction(this, entity)
    }

//    init {
//        add(LivingEntityComponent(this, monster.maximumHitPoints))
//        add(InventoryHolderComponent(this))
//    }
}
