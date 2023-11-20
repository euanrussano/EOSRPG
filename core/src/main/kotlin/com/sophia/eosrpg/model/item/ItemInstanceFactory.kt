package com.sophia.eosrpg.model.item

import com.badlogic.gdx.utils.Pool

class ItemInstanceFactory(
    val itemRepository : ItemRepository
) {

    fun createItemInstance(name : String) : ItemInstance {
        val item = itemRepository.findByName(name)
        return ItemInstance(item)
    }
}
