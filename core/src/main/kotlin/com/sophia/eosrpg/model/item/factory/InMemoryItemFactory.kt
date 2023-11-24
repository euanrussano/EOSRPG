package com.sophia.eosrpg.model.item.factory

import com.sophia.eosrpg.model.item.DamageItemComponent
import com.sophia.eosrpg.model.item.Item
import com.sophia.eosrpg.model.item.ItemRepository
import com.sophia.eosrpg.model.item.RecoverHealthItemComponent

class InMemoryItemFactory (
    override val itemRepository : ItemRepository
) : ItemFactory{

    init {
        val items = listOf<Item>(
            Item("Pointy Stick", 1).apply { add(DamageItemComponent(1,2)) },
            Item("Rusty Sword", 5).apply { add(DamageItemComponent(1,3)) },
            Item("Snake Bite", 0).apply { add(DamageItemComponent(1,2)) },
            Item("Snake Fang", 1),
            Item("Snake Skin", 2),
            Item("Rat Bite", 0).apply { add(DamageItemComponent(1,2)) },
            Item("Rat Tail", 1),
            Item("Rat Fur", 2),
            Item("Spider Bite", 0).apply { add(DamageItemComponent(1,4)) },
            Item("Spider Fang", 1),
            Item("Spider Silk", 2),
            Item("Granola Bar", 1).apply { add(RecoverHealthItemComponent(2)) },
            Item("Oats", 1),
            Item("Honey", 2),
            Item("Raisins", 2),
        )

        itemRepository.saveAll(items)
    }
}
