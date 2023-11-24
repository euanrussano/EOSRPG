package com.sophia.eosrpg.model.quest

import com.badlogic.gdx.utils.XmlReader
import com.sophia.eosrpg.model.item.Item
import com.sophia.eosrpg.model.item.ItemRepository
import ktx.assets.toInternalFile

class XMLQuestFactory(
    val questRepository: QuestRepository,
    val itemRepository: ItemRepository
) {
    val xmlReader  = XmlReader()
    val xmlFile = "quest/quest.xml".toInternalFile()

    init {
        val quests = readXML()
        questRepository.saveAll(*quests.toTypedArray())
    }

    private fun readXML(): List<Quest> {
        val quests = mutableListOf<Quest>()

        val questsRoot = xmlReader.parse(xmlFile)
        for (idx in 0 until questsRoot.childCount){
            val questElement = questsRoot.getChild(idx)
            val name = questElement.getChildByName("name").text
            val description = questElement.getChildByName("description").text

            val questTask = CompositeQuestTask()
            val taskElementRoot = questElement.getChildByName("task")
            for (idx1 in 0 until taskElementRoot.childCount){
                val taskElement = taskElementRoot.getChild(idx1)
                when(taskElement.name){
                    "collectItems" -> {
                        val itemQtyMap = mutableMapOf<String, Int>()
                        for (idx2 in 0 until taskElement.childCount){
                            val itemQtyElement = taskElement.getChild(idx2)
                            val itemName = itemQtyElement.getAttribute("name")
                            val itemQty = itemQtyElement.getAttribute("quantity").toInt()
                            itemQtyMap[itemName] = itemQty
                        }
                        questTask.children.add(CollectItemsQuestTask(itemQtyMap))
                    }
                }
            }

            val questReward = CompositeQuestReward()
            val rewardElementRoot = questElement.getChildByName("reward")

            for (idx1 in 0 until rewardElementRoot.childCount){
                val rewardElement = rewardElementRoot.getChild(idx1)
                when(rewardElement.name){
                    "xp" -> {
                        val quantity = rewardElement.getAttribute("quantity").toInt()
                        questReward.children.add(XPQuestReward(quantity))
                    }
                    "gold" ->{
                        val quantity = rewardElement.getAttribute("quantity").toInt()
                        questReward.children.add(GoldQuestReward(quantity))
                    }
                    "items" ->{
                        val itemQtyMap = mutableMapOf<Item, Int>()
                        for (idx2 in 0 until rewardElement.childCount){
                            val itemElement = rewardElement.getChild(idx2)
                            val itemName = itemElement.getAttribute("name")
                            val quantity = itemElement.getAttribute("quantity").toInt()

                            val item = itemRepository.findByName(itemName)
                            itemQtyMap[item] = quantity
                        }
                        questReward.children.add(ItemQuestReward(itemQtyMap))
                    }
                }
            }

            val quest = Quest(name, description, questTask, questReward)
            quests.add(quest)
        }
        return quests
    }
}
