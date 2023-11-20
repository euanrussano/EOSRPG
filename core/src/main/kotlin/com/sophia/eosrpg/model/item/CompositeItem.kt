package com.sophia.eosrpg.model.item

import kotlin.reflect.KClass

class CompositeItem : Item {

    val children = mutableListOf<Item> ()

    override val name: String
        get() = children.filterIsInstance<BaseItem>().first().name
    override val price: Int
        get() = children.filterIsInstance<BaseItem>().first().price

    fun add(item: Item) {
        children.add(item)
    }

    fun has(kClass: KClass<out Item>) : Boolean{
        return children.filter{ item -> kClass.isInstance(item) }.isNotEmpty()
    }

    fun get(kClass: KClass<out Item>): Item {
        return children.first{ item -> kClass.isInstance(item) }
    }
}
