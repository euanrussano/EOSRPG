package com.sophia.eosrpg.model.monster

import com.sophia.eosrpg.model.Inventory
import com.sophia.eosrpg.model.InventoryHolder

class MonsterInstance(
    val monster : Monster,
) : InventoryHolder {

    var listener: MonsterListener? = null
    override val inventory = Inventory(this)

    interface MonsterListener {
        fun updateMonsterInstance(monsterInstance: MonsterInstance)

    }

    var hitPoints : Int = monster.maximumHitPoints
        set(value) {
            field = value
            listener?.updateMonsterInstance(this)
        }
}
