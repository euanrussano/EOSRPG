package com.sophia.eosrpg.model.location

import com.sophia.eosrpg.model.quest.QuestRepository
import com.sophia.eosrpg.model.trader.TraderRepository

class LocationFactory(
    val locationRepository: LocationRepository,
    val questRepository: QuestRepository,
    val traderRepository: TraderRepository
) {
    init {
        val locations = listOf(
            Location(-2, -1, "Farmer's Field",
                "There are rows of corn growing here, with giant rats hiding between them.",
                monsterProbability = mapOf("Rat" to 100)
            ),
            Location(-1, -1, "Farmer's House",
                "This is the house of your neighbor, Farmer Ted.",
                traderHere = traderRepository.findByName("Farmer Ted")
            ),
            Location(0, -1, "Home",
            "This is your home",
            ),
            Location(-1, 0, "Trading Shop",
            "The shop of Susan, the trader.",
                traderHere = traderRepository.findByName("Susan")
            ),
            Location(0, 0, "Town Square",
            "You see a fountain here.",
            ),
            Location(1, 0, "Town Gate",
            "There is a gate here, protecting the town from giant spiders.",
            ),
            Location(2, 0, "Spider Forest",
            "The trees in this forest are covered with spider webs.",
                monsterProbability = mapOf("Spider" to 100)
            ),
            Location(0, 1, "Herbalist's Hut",
            "You see a small hut, with plants drying from the roof.",
                    listOf(questRepository.findByName("Clear the herb garden")),
                    traderHere = traderRepository.findByName("Pete the Herbalist")
            ),
            Location(0, 2, "Herbalist's Garden",
            "There are many plants here, with snakes hiding behind them.",
                monsterProbability = mapOf("Snake" to 100)
            )
        )
        locationRepository.saveAll(*locations.toTypedArray())
    }

}
