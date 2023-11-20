package com.sophia.eosrpg.model.trader

class TraderRepository {

    val traders = mutableListOf<Trader>()

    fun save(trader: Trader) {
        traders.add(trader)
    }

    fun findByName(name : String) : Trader{
        return traders.first { trader: Trader -> trader.name == name }
    }

}
