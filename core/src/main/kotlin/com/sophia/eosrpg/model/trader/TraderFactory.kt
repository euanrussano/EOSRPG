package com.sophia.eosrpg.model.trader

import com.sophia.eosrpg.model.item.ItemInstanceFactory


class TraderFactory(
    val traderRepository: TraderRepository,
    val itemInstanceFactory: ItemInstanceFactory,
) {

    init {
        val susan = Trader("Susan")
        susan.inventory.addItemInstanceToInventory(itemInstanceFactory.createItemInstance("Rusty Sword"))
        val farmerTed = Trader("Farmer Ted")
        farmerTed.inventory.addItemInstanceToInventory(itemInstanceFactory.createItemInstance("Rusty Sword"))
        val peteTheHerbalist = Trader("Pete the Herbalist")
        peteTheHerbalist.inventory.addItemInstanceToInventory(itemInstanceFactory.createItemInstance("Rusty Sword"))
        traderRepository.save(susan)
        traderRepository.save(farmerTed)
        traderRepository.save(peteTheHerbalist)
    }
}
