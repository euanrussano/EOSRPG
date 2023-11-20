package com.sophia.eosrpg

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.sophia.eosrpg.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use
import ktx.scene2d.Scene2DSkin

class EOSRPG : Game() {
    override fun create() {
        Scene2DSkin.defaultSkin = Skin("ui/uiskin.json".toInternalFile())
        //Scene2DSkin.defaultSkin.get(LabelStyle::class.java).fontColor = Color.BLACK


        setScreen(GameScreen(this))
    }
}
