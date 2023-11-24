package com.sophia.eosrpg.model.item.factory

import com.sophia.eosrpg.model.item.DamageItemComponent
import com.sophia.eosrpg.model.item.Item
import com.sophia.eosrpg.model.item.ItemRepository
import com.sophia.eosrpg.model.item.RecoverHealthItemComponent
import com.sophia.eosrpg.model.skill.SkillRepository

class InMemoryItemFactory (
    override val itemRepository : ItemRepository,
    override val skillRepository: SkillRepository
) : ItemFactory{

    init {
        val items = listOf<Item>(
            Item("Pointy Stick", 1).apply { add(DamageItemComponent(1,2, skillToUse = skillRepository.findByName("Sword"))) },
            Item("Rusty Sword", 5).apply { add(DamageItemComponent(1,3, skillToUse = skillRepository.findByName("Sword"))) },
            Item("Snake Bite", 0).apply { add(DamageItemComponent(1,2, skillToUse = skillRepository.findByName("Bite"))) },
            Item("Snake Fang", 1),
            Item("Snake Skin", 2),
            Item("Rat Bite", 0).apply { add(DamageItemComponent(1,2, skillToUse = skillRepository.findByName("Bite"))) },
            Item("Rat Tail", 1),
            Item("Rat Fur", 2),
            Item("Spider Bite", 0).apply { add(DamageItemComponent(1,4, skillToUse = skillRepository.findByName("Bite"))) },
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
