package com.sophia.eosrpg.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.sophia.eosrpg.EOSRPG
import com.sophia.eosrpg.model.Hero
import com.sophia.eosrpg.model.Location
import com.sophia.eosrpg.model.item.ItemInstance
import com.sophia.eosrpg.model.monster.MonsterInstance
import com.sophia.eosrpg.model.quest.Quest
import com.sophia.eosrpg.presenter.GamePresenter
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.actors.txt
import ktx.scene2d.*

class GameScreen(val game: EOSRPG) : Screen {


    private lateinit var weaponsTable: Table

    private lateinit var gameLogTable: Table

    private lateinit var monsterTable : Table
    private lateinit var monsterNameLbl: Label
    private lateinit var monsterHPLbl: Label
    private lateinit var monsterImage: Image

    private lateinit var locationNameLbl: Label
    private lateinit var locationImage: Image
    private lateinit var locationDescriptionLbl: Label

    private lateinit var moveNorthBtn : Button
    private lateinit var moveSouthBtn : Button
    private lateinit var moveWestBtn : Button
    private lateinit var moveEastBtn : Button

    private lateinit var heroWeaponsSelectBox: KSelectBox<String>
    private lateinit var heroNameLbl: Label
    private lateinit var heroClassLbl: Label
    private lateinit var heroHPLbl: Label
    private lateinit var heroGoldLbl: Label
    private lateinit var heroXPLbl: Label
    private lateinit var heroLevelLbl: Label

    private lateinit var inventoryTable : Table
    private lateinit var questsTable : Table

    private val uiStage = Stage(ScreenViewport())
    private lateinit var gamePresenter : GamePresenter

    override fun show() {

        inventoryTable = scene2d.table {
            background = skin.getDrawable("white")
            this.top().left()
            this.defaults().pad(2f)
        }
        questsTable = scene2d.table {
            background = skin.getDrawable("white")
            this.top().left()
            this.defaults().pad(2f)
        }
        val containerTable = scene2d.table {
        }

        uiStage.actors {
            table {
                setFillParent(true)
                table {
                    it.growX()
                    this.left()
                    this.defaults().pad(2f)
                    it.colspan(2)
                    background = skin.newDrawable("white", Color.GRAY)
                    label("Menu")
                }
                row()
                // hero Data
                table {
                    it.grow().width(Value.maxWidth)
                    this.left().top()
                    this.defaults().left().pad(2f)
                    background = skin.newDrawable("white", Color.CHARTREUSE)
                    label("Name: "); label(""){ heroNameLbl = this}
                    row()
                    label("Class: "); label(""){ heroClassLbl = this}
                    row()
                    label("Hit Points: "); label(""){ heroHPLbl = this}
                    row()
                    label("Gold: "); label(""){ heroGoldLbl = this}
                    row()
                    label("XP: "); label(""){ heroXPLbl = this}
                    row()
                    label("Level: "); label(""){ heroLevelLbl = this}
                }
                // Game Data
                table {
                    it.grow()
                    this.defaults().pad(5f)
                    background = skin.newDrawable("white", Color.SCARLET)
                    scrollPane {
                        it.grow()
                        this.setScrollingDisabled(true, false)
                        table {
                            gameLogTable = this
                        }

                    }
                    table {
                        it.grow().width(Value.maxWidth)
                        // location
                        table {
                            it.grow()
                            background = skin.getDrawable("black")
                            this.defaults().pad(1f)
                            table {
                                it.grow()
                                this.top()
                                this.defaults().pad(5f)
                                background = skin.newDrawable("white", Color.SCARLET)
                                label(""){ locationNameLbl = this }
                                row()
                                image(skin.getDrawable("white")){
                                    it.width(50f).height(50f)
                                    locationImage = this
                                }
                                row()
                                label(""){
                                    it.growX()
                                    setAlignment(Align.center)
                                    locationDescriptionLbl = this
                                    wrap = true
                                }
                            }
                            row()
                            // monster
                            table {
                                it.grow()
                                this.top()
                                this.defaults().pad(5f)
                                background = skin.newDrawable("white", Color.SCARLET)

                                table {
                                    monsterTable = this
                                    isVisible = false

                                    label("") { monsterNameLbl = this; it.colspan(2) }
                                    row()
                                    image("white") {
                                        it.width(50f).height(50f).colspan(2)
                                        monsterImage = this
                                    }
                                    row()
                                    label("Hit Points: ")
                                    label("") {
                                        it.growX()
                                        setAlignment(Align.center)
                                        monsterHPLbl = this
                                        wrap = true
                                    }
                                }
                            }
                        }

                    }
                }
                row()
                // inventory/quests
                table {
                    it.growX().height(400f/3).width(Value.maxWidth)
                    background = skin.newDrawable("white", Color.FIREBRICK)
                    this.top().left()
                    this.defaults().pad(10f)
                    textButton("Inventory"){
                        onClick {
                            containerTable.clearChildren()
                            containerTable.add(inventoryTable).grow()
                        }
                    }
                    textButton("Quests"){
                        onClick {
                            containerTable.clearChildren()
                            containerTable.add(questsTable).grow()
                        }
                    }
                    row()
                    scrollPane {
                        it.grow().colspan(2)
                        actor = containerTable
                    }

                }
                // combat/movement controls
                table {
                    it.growX().height(400f/3)
                    //this.defaults().width(50f)
                    background = skin.newDrawable("white", Color.VIOLET)
                    table {
                        it.grow()
                        this.defaults().pad(5f)
                        weaponsTable = this
                        isVisible = false
                        selectBox<String> {
                            heroWeaponsSelectBox = this
                            onChange{

                                val weaponName = if (this.selected == "None") null else this.selected
                                gamePresenter.changeHeroCurrentWeapon(weaponName)
                            }
                        }
                        textButton("Use"){
                            onClick {
                                gamePresenter.attackCurrentMonster()
                            }
                        }
                    }
                    table {
                        it.grow()
                        add()
                        textButton("North"){
                            moveNorthBtn = this
                            onClick {
                                gamePresenter.moveHeroNorth()
                            }
                        }
                        add()
                        row()
                        textButton("West"){
                            moveWestBtn = this
                            onClick {
                                gamePresenter.moveHeroWest()
                            }
                        }
                        add()
                        textButton("East"){
                            moveEastBtn = this
                            onClick {
                                gamePresenter.moveHeroEast()
                            }
                        }
                        row()
                        add()
                        textButton("South"){
                            moveSouthBtn = this
                            onClick {
                                gamePresenter.moveHeroSouth()
                            }
                        }
                        add()
                    }
                }
            }
        }

        Gdx.input.inputProcessor = uiStage
        gamePresenter = GamePresenter(this)
        gamePresenter.initialize()
    }


    override fun render(delta: Float) {
        ScreenUtils.clear(Color.BLACK)

        uiStage.viewport.apply()
        uiStage.act(delta)
        uiStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        uiStage.viewport.update(width, height, true)
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun hide() {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }

    fun updateHero(currentHero: Hero) {
        heroNameLbl.txt = currentHero.name
        heroClassLbl.txt = currentHero.characterClass
        heroLevelLbl.txt = currentHero.level.toString()
        heroHPLbl.txt = currentHero.hitPoints.toString()
        heroGoldLbl.txt = currentHero.gold.toString()
        heroXPLbl.txt = currentHero.experiencePoints.toString()

        updateHeroInventory(currentHero.inventory)
        updateHeroQuests(currentHero.questStatus)
        updateHeroWeapons(currentHero.weapons)
        updateHeroCurrentWeapon(currentHero.currentWeapon)
    }

    fun updateHeroQuests(questStatus : Map<Quest, Boolean>) {
        questsTable.clearChildren()
        questsTable.add("Name").growX()
        questsTable.add("Done?")
        questsTable.row()
        for ((quest, isDone) in questStatus) {
            questsTable.add(quest.name).growX()
            questsTable.add(if(isDone) "Yes" else "False")
            questsTable.row()
        }
    }

    fun updateHeroInventory(inventory: List<ItemInstance>) {
        inventoryTable.clearChildren()
        inventoryTable.add("Name").growX()
        inventoryTable.add("Price")
        inventoryTable.row()
        for (itemInstance in inventory) {
            inventoryTable.add(itemInstance.item.name).growX()
            inventoryTable.add(itemInstance.item.price.toString())
            inventoryTable.row()
        }
        updateHeroWeapons(inventory)
    }

    fun updateHeroWeapons(inventory: List<ItemInstance>) {
        val weapons = inventory.map { itemInstance -> itemInstance.item.name }.toTypedArray()
        heroWeaponsSelectBox.setItems("None", *weapons)

    }
    fun updateHeroCurrentWeapon(currentWeapon: ItemInstance?) {
        heroWeaponsSelectBox.selected =currentWeapon?.item?.name
    }

    fun updateLocation(currentLocation: Location) {
        locationNameLbl.txt = currentLocation.name
        val locationTextureName = "location/" + currentLocation.name.replace("'","").replace(" ", "") + ".png"
        val locationTexture = Texture(locationTextureName)
        locationImage.drawable = TextureRegionDrawable(locationTexture)
        locationDescriptionLbl.txt = currentLocation.description

        moveNorthBtn.isVisible = gamePresenter.hasLocationNorth
        moveSouthBtn.isVisible = gamePresenter.hasLocationSouth
        moveWestBtn.isVisible = gamePresenter.hasLocationWest
        moveEastBtn.isVisible = gamePresenter.hasLocationEast
    }

    fun updateMonsterInstance(monsterInstance: MonsterInstance) {
        monsterTable.isVisible = true
        weaponsTable.isVisible = true
        monsterNameLbl.txt = monsterInstance.monster.name
        val monsterTexture = Texture("monster/" + monsterInstance.monster.name + ".png")
        monsterImage.drawable = TextureRegionDrawable(monsterTexture)
        monsterHPLbl.txt = monsterInstance.hitPoints.toString()
    }

    fun clearMonsterInstance() {
        monsterTable.isVisible = false
        weaponsTable.isVisible = false
    }

    fun raiseMessage(msg : String){
        if (!gameLogTable.children.isEmpty){
            gameLogTable.row()
        }
        val label = scene2d.label(msg){
            wrap = true
        }
        gameLogTable.add(label).growX()

        val scrollPane = gameLogTable.parent as ScrollPane
        scrollPane.scrollTo(0f, scrollPane.maxY, scrollPane.width, gameLogTable.height)
    }

    fun updateHeroGold(gold: Int) {
        heroGoldLbl.txt = gold.toString()
    }

    fun updateHeroHitPoints(hitPoints: Int) {
        heroHPLbl.txt = hitPoints.toString()
    }

    fun updateHeroXP(level: Int) {
        heroXPLbl.txt = level.toString()
    }

    fun updateHeroLevel(level: Int) {
        heroLevelLbl.txt = level.toString()
    }


}
