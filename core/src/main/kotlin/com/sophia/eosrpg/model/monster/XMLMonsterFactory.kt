package com.sophia.eosrpg.model.monster

import com.badlogic.gdx.utils.XmlReader
import com.sophia.eosrpg.model.skill.Skill
import com.sophia.eosrpg.model.item.ItemRepository
import com.sophia.eosrpg.model.skill.SkillRepository
import ktx.assets.toInternalFile

class XMLMonsterFactory(
    val monsterRepository: MonsterRepository,
    val itemRepository: ItemRepository,
    val skillRepository: SkillRepository
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
            val monsterElement = monstersRoot.getChild(idx)
            val name = monsterElement.getChildByName("name").text
            val maximumHitPoints = monsterElement.getChildByName("maximumHitPoints").text.toInt()
            val rewardXP = monsterElement.getChildByName("rewardXP").text.toInt()
            val rewardGold = monsterElement.getChildByName("rewardGold").text.toInt()
            val weaponName = monsterElement.getChildByName("weapon").getAttribute("name")

            val lootItemProbabilityMap = mutableMapOf<String, Int>()

            if (monsterElement.hasChild("lootItems")){
                val lootItemsElementRoot = monsterElement.getChildByName("lootItems")
                for (idx1 in 0 until lootItemsElementRoot.childCount){
                    val lootItemElement = lootItemsElementRoot.getChild(idx1)
                    val itemName = lootItemElement.getAttribute("name")
                    val itemProbability = lootItemElement.getAttribute("probability").toInt()
                    lootItemProbabilityMap[itemName] = itemProbability
                }
            }

            val skills = mutableMapOf<Skill, Int>()

            monsterElement.getChildByName("skills")?.let{skillsRoot ->
                for (idx1 in 0 until skillsRoot.childCount){
                    val skillElement = skillsRoot.getChild(idx1)
                    val skillname = skillElement.getChildByName("name").text
                    val skillLevel = skillElement.getChildByName("value").text.toInt()
                    val skill = skillRepository.findByName(skillname)
                    skills[skill] = skillLevel
                }
            }



            val monster = Monster(name, maximumHitPoints, rewardXP, rewardGold, lootItemProbabilityMap, itemRepository.findByName(weaponName), skills)
            monsters.add(monster)
        }
        return monsters
    }

}
