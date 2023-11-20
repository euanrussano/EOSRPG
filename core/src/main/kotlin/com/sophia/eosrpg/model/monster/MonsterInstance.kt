package com.sophia.eosrpg.model.monster

class MonsterInstance(
    val monster : Monster,
    val inventory : Map<String, Int>
) {
    var listener: MonsterListener? = null

    interface MonsterListener {
        fun updateMonsterInstance(monsterInstance: MonsterInstance)

    }

    var hitPoints : Int = monster.maximumHitPoints
        set(value) {
            field = value
            listener?.updateMonsterInstance(this)
        }
}
