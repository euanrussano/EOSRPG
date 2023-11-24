package com.sophia.eosrpg.model.item

import com.sophia.eosrpg.model.entity.Entity
import kotlin.reflect.KClass

class Item(
    override val name: String,
    val basePrice : Int,
) : BaseItem{

    private val components = mutableListOf<ItemComponent> ()

    override val price: Int
        get() = if (components.size > 0) (components.map { it.getPriceMultiplier() }.sum()*basePrice).toInt() else basePrice

    fun add(item: ItemComponent) {
        components.add(item)
        item.parent = this
    }

    fun has(kClass: KClass<out ItemComponent>) : Boolean{
        return components.filter{ item -> kClass.isInstance(item) }.isNotEmpty()
    }

    fun get(kClass: KClass<out ItemComponent>): ItemComponent {
        return components.first{ item -> kClass.isInstance(item) }
    }

    override fun performAction(actor : Entity, target : Entity){
        components.forEach {
            it.performAction(actor, target)
        }
    }
}
