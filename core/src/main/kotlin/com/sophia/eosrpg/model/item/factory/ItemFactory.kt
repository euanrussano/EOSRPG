package com.sophia.eosrpg.model.item.factory

import com.sophia.eosrpg.model.item.ItemRepository
import com.sophia.eosrpg.model.skill.SkillRepository

interface ItemFactory {
    val itemRepository : ItemRepository
    val skillRepository : SkillRepository
}
