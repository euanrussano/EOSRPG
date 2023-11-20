package com.sophia.eosrpg.model.item

import kotlin.reflect.KClass

class Item(
    val name: String,
    val basePrice : Int
) {

    private val components = mutableListOf<ItemComponent> ()
    val price: Int
        get() = if (components.size > 0) (components.map { it.getPriceMultiplier() }.sum()*basePrice).toInt() else basePrice

    fun add(item: ItemComponent) {
        components.add(item)
    }

    fun has(kClass: KClass<out ItemComponent>) : Boolean{
        return components.filter{ item -> kClass.isInstance(item) }.isNotEmpty()
    }

    fun get(kClass: KClass<out ItemComponent>): ItemComponent {
        return components.first{ item -> kClass.isInstance(item) }
    }
}
