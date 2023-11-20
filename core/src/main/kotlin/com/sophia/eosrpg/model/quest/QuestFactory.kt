package com.sophia.eosrpg.model.quest

import com.sophia.eosrpg.model.item.ItemRepository

class QuestFactory(
    val questRepository: QuestRepository,
    val itemRepository: ItemRepository
) {
    init {
        val quest = Quest(
            "Clear the herb garden",
            "Defeat the snakes in the Herbalist's garden",
            CollectItemsQuestTask(mapOf("Snake Fang" to 2)),
            CompositeQuestReward(
                XPQuestReward(25),
                GoldQuestReward(10),
                ItemQuestReward(
                    mapOf(itemRepository.findByName("Rusty Sword") to 1)
                )
            )
        )
        questRepository.save(quest)
    }
}
