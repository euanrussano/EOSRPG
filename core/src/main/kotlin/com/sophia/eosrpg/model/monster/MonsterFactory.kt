package com.sophia.eosrpg.model.monster

class MonsterFactory(
    val monsterRepository: MonsterRepository
) {
    init {
        monsterRepository.saveAll(
            Monster("Snake", 1,2,4, 5, 1, mapOf("Snake Fang" to 100)), //, "Snake Skin" to 75
            Monster("Rat", 1,2,5, 5, 1, mapOf("Rat Tail" to 25, "Rat Fur" to 75)),
            Monster("Spider", 1, 4, 10, 10, 3, mapOf("Spider Fang" to 25, "Spider Silk" to 75)),
        )
    }
}
