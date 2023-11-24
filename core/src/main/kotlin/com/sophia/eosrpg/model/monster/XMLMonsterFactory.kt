package com.sophia.eosrpg.model.monster

import com.badlogic.gdx.utils.XmlReader
import com.sophia.eosrpg.model.item.ItemRepository
import com.sophia.eosrpg.model.quest.Quest
import ktx.assets.toInternalFile

class XMLMonsterFactory(
    val monsterRepository: MonsterRepository,
    val itemRepository: ItemRepository,
) {
    val xmlReader  = XmlReader()
    val xmlFile = "monster/monster.xml".toInternalFile()

    init {
        val monsters = readXML()
        monsterRepository.saveAll(*monsters.toTypedArray())
    }

    private fun readXML() : List<Monster>{
        val monsters = mutableListOf<Monster>()

        val monstersRoot = xmlReader.parse(xmlFile)
        for (idx in 0 until monstersRoot.childCount){
            val locationElement = monstersRoot.getChild(idx)
            val name = locationElement.getChildByName("name").text
            val maximumHitPoints = locationElement.getChildByName("maximumHitPoints").text.toInt()
            val rewardXP = locationElement.getChildByName("rewardXP").text.toInt()
            val rewardGold = locationElement.getChildByName("rewardGold").text.toInt()
            val weaponName = locationElement.getChildByName("weapon").getAttribute("name")

            val lootItemProbabilityMap = mutableMapOf<String, Int>()

            if (locationElement.hasChild("lootItems")){
                val lootItemsElementRoot = locationElement.getChildByName("lootItems")
                for (idx1 in 0 until lootItemsElementRoot.childCount){
                    val lootItemElement = lootItemsElementRoot.getChild(idx1)
                    val itemName = lootItemElement.getAttribute("name")
                    val itemProbability = lootItemElement.getAttribute("probability").toInt()
                    lootItemProbabilityMap[itemName] = itemProbability
                }
            }

            val monster = Monster(name, maximumHitPoints, rewardXP, rewardGold, lootItemProbabilityMap, itemRepository.findByName(weaponName))
            monsters.add(monster)
        }
        return monsters
    }

}
