package com.sophia.eosrpg.model.monster

import com.sophia.eosrpg.model.item.ItemRepository

class MonsterFactory(
    val monsterRepository: MonsterRepository,
    val itemRepository: ItemRepository
) {
    init {
        monsterRepository.saveAll(
            Monster("Snake", 4, 5, 1, mapOf("Snake Fang" to 100), itemRepository.findByName("Snake Bite")), //, "Snake Skin" to 75
            Monster("Rat", 5, 5, 1, mapOf("Rat Tail" to 25, "Rat Fur" to 75), itemRepository.findByName("Rat Bite")),
            Monster("Spider",  10, 10, 3, mapOf("Spider Fang" to 25, "Spider Silk" to 75), itemRepository.findByName("Spider Bite")),
        )
    }
}
