package com.sophia.eosrpg.model.quest

import com.sophia.eosrpg.model.Hero
import com.sophia.eosrpg.model.InventoryHolderComponent

class CollectItemsQuestTask(
    val itemsToComplete : Map<String, Int>
) : QuestTask {
    override fun canPerform(hero: Hero): Boolean {
        val heroInventory = InventoryHolderComponent.get(hero)
        return heroInventory.hasAllItems(itemsToComplete)
    }

    override fun perform(hero: Hero) {
        for ((itemName, qty) in itemsToComplete) {
            for (i in 0 until qty){
                val heroInventory = InventoryHolderComponent.get(hero)
                val itemInstance1 = heroInventory.findByName(itemName)
                heroInventory.removeItemInstanceToInventory(itemInstance1)
            }
        }

    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Collect the items:\n")
        for ((itemName, qty) in itemsToComplete) {
            stringBuilder.append("$qty $itemName\n")
        }
        return stringBuilder.toString()
    }
}
