package com.sophia.eosrpg.model.item

class ItemInstanceFactory(
    val itemRepository : ItemRepository
) {

    fun createItemInstance(name : String) : ItemInstance {
        val item = itemRepository.findByName(name)
        return ItemInstance(item)
    }
}
