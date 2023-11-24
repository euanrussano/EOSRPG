package com.sophia.eosrpg.model.item.factory

import com.badlogic.gdx.utils.XmlReader
import com.sophia.eosrpg.model.item.*
import ktx.assets.toInternalFile

class XMLItemFactory(
    override val itemRepository: ItemRepository
) : ItemFactory{

    val xmlReader  = XmlReader()
    val xmlFile = "item/item.xml".toInternalFile()

    init {
        readXML()
    }

    private fun readXML() {
        val items = xmlReader.parse(xmlFile)
        for (idx in 0 until items.childCount){
            val itemElement = items.getChild(idx)
            val name = itemElement.getChildByName("name").text
            val price = itemElement.getChildByName("price").text.toInt()
            val components = mutableListOf<ItemComponent>()

            if (itemElement.hasChild("components")){
                val componentElements = itemElement.getChildByName("components")
                for (idx1 in 0 until componentElements.childCount){
                    val componentElement = componentElements.getChild(idx1)
                    when (componentElement.name){
                        "damageItemComponent" -> {
                            val minimumDamage = componentElement.getChildByName("minimumDamage").text.toInt()
                            val maximumDamage = componentElement.getChildByName("maximumDamage").text.toInt()
                            components.add(DamageItemComponent(minimumDamage, maximumDamage))
                        }
                        "recoverHealthItemComponent" -> {
                            val amountToRecover = componentElement.getChildByName("amountToRecover").text.toInt()
                            components.add(RecoverHealthItemComponent(amountToRecover))
                        }
                        else -> throw Error("component <${componentElement.name}> in ( $xmlFile )  could not be read in ${this::class.simpleName}")
                    }
                }
            }
            val item = Item(name, price)
            components.forEach { item.add(it) }
            itemRepository.save(item)
        }
    }
}
