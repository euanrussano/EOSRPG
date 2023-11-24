package com.sophia.eosrpg.presenter

import com.badlogic.gdx.utils.Json
import com.sophia.eosrpg.model.GameState
import ktx.assets.file
import ktx.assets.toExternalFile

object SaveGameService {

    fun save(gameState: GameState) {
        val json = Json()
        val gameStateJson = json.prettyPrint(gameState)
        val fileSave = "savegame.json".toExternalFile()
        fileSave.writeString(gameStateJson, false)
        println("saved!")

    }


}
