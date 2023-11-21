package com.sophia.eosrpg.model.monster

import com.sophia.eosrpg.model.Entity
import com.sophia.eosrpg.model.Inventory
import com.sophia.eosrpg.model.InventoryHolder
import com.sophia.eosrpg.model.LivingEntityComponent
import com.sophia.eosrpg.model.item.ItemInstance

class MonsterInstance(
    val monster : Monster,
) : InventoryHolder, Entity() {

    val weapon = ItemInstance(monster.weapon)

    fun useCurrentWeaponOn(entity: Entity) {
        weapon.performAction(this, entity)
    }

    var listener: MonsterListener? = null
    override val inventory = Inventory(this)

    interface MonsterListener {
        fun updateMonsterInstance(monsterInstance: MonsterInstance)

    }

    init {
        add(LivingEntityComponent(this, monster.maximumHitPoints))
    }

//    var hitPoints : Int = monster.maximumHitPoints
//        set(value) {
//            field = value
//            listener?.updateMonsterInstance(this)
//        }
}
