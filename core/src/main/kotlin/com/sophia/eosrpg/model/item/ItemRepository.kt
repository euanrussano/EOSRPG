package com.sophia.eosrpg.model.item

class ItemRepository {

    val items = mutableListOf<Item>()

    fun save(item : Item){
        items.add(item)
    }

    fun findByName(name: String): Item {
        return items.first { item -> item.name == name }
    }

    fun saveAll(items: List<Item>) {
        this.items.addAll(items)
    }

}
