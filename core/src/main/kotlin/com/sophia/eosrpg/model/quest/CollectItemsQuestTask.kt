package com.sophia.eosrpg.model.quest

import com.sophia.eosrpg.model.Hero

class CollectItemsQuestTask(
    val itemsToComplete : Map<String, Int>
) : QuestTask {
    override fun canPerform(hero: Hero): Boolean {
        return hero.hasAllItems(itemsToComplete)
    }

    override fun perform(hero: Hero) {
        for ((itemName, qty) in itemsToComplete) {
            val itemInstance1 = hero.inventory.first { itemInstance -> itemInstance.item.name == itemName }
            hero.removeItemInstanceToInventory(itemInstance1)
        }

    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        for ((itemName, qty) in itemsToComplete) {
            stringBuilder.append("$qty $itemName\n")
        }
        return stringBuilder.toString()
    }
}
