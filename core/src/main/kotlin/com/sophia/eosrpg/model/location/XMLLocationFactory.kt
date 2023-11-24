package com.sophia.eosrpg.model.location

import com.badlogic.gdx.utils.XmlReader
import com.sophia.eosrpg.model.quest.Quest
import com.sophia.eosrpg.model.quest.QuestRepository
import com.sophia.eosrpg.model.trader.Trader
import com.sophia.eosrpg.model.trader.TraderRepository
import ktx.assets.toInternalFile

class XMLLocationFactory(
    val locationRepository: LocationRepository,
    val questRepository: QuestRepository,
    val traderRepository: TraderRepository
) {
    val xmlReader  = XmlReader()
    val xmlFile = "location/location.xml".toInternalFile()

    init {
        val locations = readXML()
        locationRepository.saveAll(*locations.toTypedArray())
    }

    private fun readXML() : List<Location>{
        val locations = mutableListOf<Location>()

        val locationsRoot = xmlReader.parse(xmlFile)
        for (idx in 0 until locationsRoot.childCount){
            val locationElement = locationsRoot.getChild(idx)
            val x = locationElement.getChildByName("x").text.toInt()
            val y = locationElement.getChildByName("y").text.toInt()
            val name = locationElement.getChildByName("name").text
            val description = locationElement.getChildByName("description").text

            val monsterProbabilityMap = mutableMapOf<String, Int>()

            if (locationElement.hasChild("monsters")){
                val monsterElements = locationElement.getChildByName("monsters")
                for (idx1 in 0 until monsterElements.childCount){
                    val monsterElement = monsterElements.getChild(idx1)
                    val monsterName = monsterElement.getAttribute("name")
                    val monsterProbability = monsterElement.getAttribute("probability").toInt()
                    monsterProbabilityMap[monsterName] = monsterProbability
                }
            }

            val quests = mutableListOf<Quest>()
            if (locationElement.hasChild("quests")){
                val questElements = locationElement.getChildByName("quests")
                for (idx1 in 0 until questElements.childCount){
                    val questElement = questElements.getChild(idx1)
                    val questName = questElement.getAttribute("name")
                    quests.add(questRepository.findByName(questName))
                }
            }

            var traderHere : Trader? = null
            locationElement.getChildByName("trader")?.let { traderElement ->
                val traderName = traderElement.getAttribute("name")
                traderHere = traderRepository.findByName(traderName)
            }

            val location = Location(x, y, name, description, quests, monsterProbabilityMap, traderHere)
            locations.add(location)
        }
        return locations
    }

}
