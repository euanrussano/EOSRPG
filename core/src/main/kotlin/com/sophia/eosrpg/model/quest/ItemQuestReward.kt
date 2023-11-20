package com.sophia.eosrpg.model.quest

import com.sophia.eosrpg.model.Hero
import com.sophia.eosrpg.model.item.Item
import com.sophia.eosrpg.model.item.ItemInstance

class ItemQuestReward(
    val rewardItems : Map<Item, Int>
) : QuestReward {

    override fun giveTo(hero: Hero) {
        for ((rewardItem, qty) in rewardItems) {
            for (i in 0 until qty){
                hero.addItemInstanceToInventory(ItemInstance(rewardItem))
            }
        }
    }

    override fun toString(): String {
        var stringOut = ""
        rewardItems.forEach { item, qty ->
            stringOut = stringOut.plus("$qty ${item.name}\n")
        }
        return stringOut
    }
}
