package com.sophia.eosrpg.model

class World(
    val locations : List<Location>
) {
    fun locationAt(x: Int, y: Int): Location? {
        return locations.firstOrNull{ location: Location -> location.x == x && location.y == y }
    }
}
