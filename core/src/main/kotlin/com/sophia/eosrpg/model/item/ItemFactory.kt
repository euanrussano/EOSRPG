package com.sophia.eosrpg.model.item

class ItemFactory (
    val itemRepository : ItemRepository
){

    init {
        val items = listOf<Item>(
            CompositeItem().apply { add(BaseItem("Pointy Stick", 1)); add(DamageItem(1,2)) },
            CompositeItem().apply { add(BaseItem("Rusty Sword", 5)); add(DamageItem(1,3)) },
            BaseItem("Snake Fang", 1),
            BaseItem("Snake Skin", 2),
            BaseItem("Rat Tail", 1),
            BaseItem("Rat Fur", 2),
            BaseItem("Spider Fang", 1),
            BaseItem("Spider Silk", 2),
        )

        itemRepository.saveAll(items)
    }
}
