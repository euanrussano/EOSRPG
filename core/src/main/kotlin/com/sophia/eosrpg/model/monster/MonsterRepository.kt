package com.sophia.eosrpg.model.monster

class MonsterRepository  {
    val monsters = mutableListOf<Monster>()

    fun saveAll(vararg monsters1: Monster){
        monsters.addAll(monsters1)
    }

    fun findByName(name: String): Monster {
        return monsters.first { monster -> monster.name == name }
    }
}
