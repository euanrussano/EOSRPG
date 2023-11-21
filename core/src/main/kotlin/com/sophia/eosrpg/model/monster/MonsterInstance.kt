package com.sophia.eosrpg.model.monster

import com.sophia.eosrpg.model.*
import com.sophia.eosrpg.model.item.ItemInstance

class MonsterInstance(
    val monster : Monster,
) : Entity() { //InventoryHolder

    val weapon = ItemInstance(monster.weapon)

    fun useCurrentWeaponOn(entity: Entity) {
        weapon.performAction(this, entity)
    }

    var listener: MonsterListener? = null
//    override val inventory = Inventory(this)

    interface MonsterListener {
        fun updateMonsterInstance(monsterInstance: MonsterInstance)

    }

    init {
        add(LivingEntityComponent(this, monster.maximumHitPoints))
        add(InventoryHolderComponent(this))
    }

//    var hitPoints : Int = monster.maximumHitPoints
//        set(value) {
//            field = value
//            listener?.updateMonsterInstance(this)
//        }
}
