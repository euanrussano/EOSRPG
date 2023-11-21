package com.sophia.eosrpg.model.monster

import com.badlogic.gdx.math.MathUtils
import com.sophia.eosrpg.model.InventoryHolderComponent
import com.sophia.eosrpg.model.item.ItemInstanceFactory

class MonsterInstanceFactory(
    val monsterRepository: MonsterRepository,
    val itemInstanceFactory: ItemInstanceFactory
) {

    fun createMonsterInstance(name: String) : MonsterInstance{
        val inventory = mutableMapOf<String, Int>()
        val monster = monsterRepository.findByName(name)

        val monsterInstance = MonsterInstance(monster)
        val monsterInventory = InventoryHolderComponent.get(monsterInstance)

        var runningTotal = 0
        val p = MathUtils.random()
        for ((itemName, prob) in monster.itemProbability) {
            runningTotal += prob
            if (p <= runningTotal/100f){
                val itemInstance = itemInstanceFactory.createItemInstance(itemName)
                monsterInventory.addItemInstanceToInventory(itemInstance)
            }
        }
        return monsterInstance
    }
}
