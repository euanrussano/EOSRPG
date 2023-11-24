package com.sophia.eosrpg.model.trader

import com.badlogic.gdx.utils.XmlReader
import com.sophia.eosrpg.model.item.ItemInstanceFactory
import ktx.assets.toInternalFile


class XMLTraderFactory(
    val traderRepository: TraderRepository,
    val itemInstanceFactory: ItemInstanceFactory,
) {

    val xmlReader = XmlReader()
    val xmlFile = "trader/trader.xml".toInternalFile()

    init {
        val traders = readXML()
        for (trader in traders) {
            traderRepository.save(trader)
        }
    }

    private fun readXML(): List<Trader> {
        val traders = mutableListOf<Trader>()

        val tradersRoot = xmlReader.parse(xmlFile)
        for (idx in 0 until tradersRoot.childCount) {
            val traderElement = tradersRoot.getChild(idx)
            val name = traderElement.getChildByName("name").text

            val trader = Trader(name)
            val inventory = trader.inventory


            val inventoryRootElement = traderElement.getChildByName("inventory")
            for (idx1 in 0 until inventoryRootElement.childCount) {
                val itemElement = inventoryRootElement.getChild(idx1)
                val itemName = itemElement.getAttribute("name")
                val itemQty = itemElement.getAttribute("quantity").toInt()
                println(itemName)
                for (i in 0 until itemQty) {
                    val itemInstance = itemInstanceFactory.createItemInstance(itemName)
                    inventory.addItemInstanceToInventory(itemInstance)
                }
            }

            traders.add(trader)


        }
        return traders
    }
}
