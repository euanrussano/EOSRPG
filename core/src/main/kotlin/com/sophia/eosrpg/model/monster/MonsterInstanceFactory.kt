package com.sophia.eosrpg.model.monster

import com.badlogic.gdx.math.MathUtils

class MonsterInstanceFactory(val monsterRepository: MonsterRepository) {

    fun createMonsterInstance(name: String) : MonsterInstance{
        val inventory = mutableMapOf<String, Int>()
        val monster = monsterRepository.findByName(name)

        var runningTotal = 0
        val p = MathUtils.random()
        for ((itemName, prob) in monster.itemProbability) {
            runningTotal += prob
            if (p <= runningTotal/100f){
                inventory[itemName] = 1
            }
        }
        return MonsterInstance(monster, inventory)
    }
}
