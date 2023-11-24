package com.sophia.eosrpg.model.trader

import com.sophia.eosrpg.model.InventoryHolderComponent
import com.sophia.eosrpg.model.item.ItemInstanceFactory


class TraderFactory(
    val traderRepository: TraderRepository,
    val itemInstanceFactory: ItemInstanceFactory,
) {

    init {
        val susan = Trader("Susan")
        InventoryHolderComponent.get(susan).addItemInstanceToInventory(itemInstanceFactory.createItemInstance("Rusty Sword"))
        val farmerTed = Trader("Farmer Ted")
        InventoryHolderComponent.get(farmerTed).addItemInstanceToInventory(itemInstanceFactory.createItemInstance("Rusty Sword"))
        val peteTheHerbalist = Trader("Pete the Herbalist")
        InventoryHolderComponent.get(peteTheHerbalist).addItemInstanceToInventory(itemInstanceFactory.createItemInstance("Rusty Sword"))
        traderRepository.save(susan)
        traderRepository.save(farmerTed)
        traderRepository.save(peteTheHerbalist)
    }
}
