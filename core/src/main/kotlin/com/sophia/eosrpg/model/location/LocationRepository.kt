package com.sophia.eosrpg.model.location

class LocationRepository {

    val locations = mutableListOf<Location>()

    fun saveAll(vararg locations : Location){
        this.locations.addAll(locations)
    }

    fun locationAt(x: Int, y: Int): Location? {
        return locations.firstOrNull{ location: Location -> location.x == x && location.y == y }
    }
}
