package com.sophia.eosrpg.model.item

class ItemFactory (
    val itemRepository : ItemRepository
){

    init {
        val items = listOf<Item>(
            Item("Pointy Stick", 1).apply { add(DamageItemComponent(1,2)) },
            Item("Rusty Sword", 5).apply { add(DamageItemComponent(1,3)) },
            Item("Snake Fang", 1),
            Item("Snake Skin", 2),
            Item("Rat Tail", 1),
            Item("Rat Fur", 2),
            Item("Spider Fang", 1),
            Item("Spider Silk", 2),
        )

        itemRepository.saveAll(items)
    }
}
