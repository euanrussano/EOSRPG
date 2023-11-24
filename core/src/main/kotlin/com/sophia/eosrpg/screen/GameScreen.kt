package com.sophia.eosrpg.screen

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.sophia.eosrpg.EOSRPG
import com.sophia.eosrpg.model.Hero
import com.sophia.eosrpg.model.location.Location
import com.sophia.eosrpg.model.item.DamageItemComponent
import com.sophia.eosrpg.model.item.ItemInstance
import com.sophia.eosrpg.model.item.RecoverHealthItemComponent
import com.sophia.eosrpg.model.monster.MonsterInstance
import com.sophia.eosrpg.model.quest.Quest
import com.sophia.eosrpg.model.recipe.Recipe
import com.sophia.eosrpg.model.trader.Trader
import com.sophia.eosrpg.presenter.GamePresenter
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.actors.txt
import ktx.scene2d.*

class GameScreen(val game: EOSRPG) : Screen, InputAdapter() {

    private lateinit var containerTable : Table

    private lateinit var recipesTable : Table

    private lateinit var tradeButton : Button
    private lateinit var tradeWindow: TradeWindow

    private lateinit var weaponsTable: Table
    private lateinit var heroWeaponsSelectBox: KSelectBox<String>

    private lateinit var consumableTable: Table
    private lateinit var heroConsumablesSelectBox: KSelectBox<String>

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


    private lateinit var heroNameLbl: Label
    private lateinit var heroClassLbl: Label
    private lateinit var heroHPLbl: Label
    private lateinit var heroGoldLbl: Label
    private lateinit var heroXPLbl: Label
    private lateinit var heroLevelLbl: Label

    private lateinit var inventoryTable : Table
    private lateinit var questsTable : Table

    private val uiStage = Stage(ExtendViewport(400f, 400f))
    private lateinit var gamePresenter : GamePresenter

    override fun show() {
        if(Gdx.app.type == Application.ApplicationType.Android){
            uiStage.viewport = ExtendViewport(400f, 400f)
        } else{
            uiStage.viewport = ScreenViewport()
        }

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
        recipesTable = scene2d.table {
            background = skin.getDrawable("white")
            this.top().left()
            this.defaults().pad(2f)
        }
        containerTable = scene2d.table {
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
                                    it.grow()
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
                            showTableOnContainer(containerTable, inventoryTable)
                        }
                    }
                    textButton("Quests"){
                        onClick {
                            showTableOnContainer(containerTable, questsTable)
                        }
                    }
                    textButton("Recipes"){
                        onClick {
                            showTableOnContainer(containerTable, recipesTable)
                        }
                    }
                    row()
                    scrollPane {
                        it.grow().colspan(3)
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
                    }
                    row()
                    table {
                        it.grow()
                        this.defaults().pad(5f)
                        consumableTable = this
                        selectBox<String> {
                            heroConsumablesSelectBox = this
                        }
                        textButton("Use"){
                            onClick {
                                gamePresenter.heroConsumeItemInstance(heroConsumablesSelectBox.selected)
                            }
                        }
                    }
                    table {
                        it.grow()
                        this.defaults().width(50f)
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
                        textButton("Trade"){
                            tradeButton = this
                            isVisible = false
                            onClick {
                                stage.addActor(tradeWindow)
                            }
                        }
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
        gamePresenter = GamePresenter(this)

        tradeWindow = TradeWindow(gamePresenter)


        val im = InputMultiplexer()
        im.addProcessor(uiStage)
        im.addProcessor(this)
        Gdx.input.inputProcessor = im

        gamePresenter.initialize()
    }

    private fun showTableOnContainer(container: Table, table : Table) {
        container.clearChildren()
        container.add(table).grow()
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
        gamePresenter.save()
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun hide() {

    }

    override fun dispose() {
        TODO("Not yet implemented")
    }

    override fun keyDown(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.W -> gamePresenter.moveHeroNorth()
            Input.Keys.A -> gamePresenter.moveHeroWest()
            Input.Keys.S -> gamePresenter.moveHeroSouth()
            Input.Keys.D -> gamePresenter.moveHeroEast()
            Input.Keys.Z -> gamePresenter.attackCurrentMonster()

            Input.Keys.I -> showTableOnContainer(containerTable, inventoryTable)
            Input.Keys.Q -> showTableOnContainer(containerTable, questsTable)
            Input.Keys.R -> showTableOnContainer(containerTable, recipesTable)

            Input.Keys.T -> uiStage.addActor(tradeWindow)


        }
        return false
    }

    fun updateHero(currentHero: Hero) {
        heroNameLbl.txt = currentHero.name
        heroClassLbl.txt = currentHero.characterClass
        heroLevelLbl.txt = currentHero.level.toString()
        heroHPLbl.txt = currentHero.health.hitPoints.toString()
        heroGoldLbl.txt = currentHero.gold.toString()
        heroXPLbl.txt = currentHero.experiencePoints.toString()

        val heroInventory = currentHero.inventory
        updateHeroInventory(heroInventory.itemInstances)
        updateHeroQuests(currentHero.questStatus)
        updateHeroWeapons(heroInventory.weapons)
        updateHeroCurrentWeapon(currentHero.currentWeapon)
        updateHeroRecipes(currentHero.recipes)

        tradeWindow.updateHeroInventory(heroInventory.itemInstances)
    }

    fun updateHeroQuests(questStatus : Map<Quest, Boolean>) {
        questsTable.clearChildren()
        questsTable.add("Name").growX()
        questsTable.add("Done?")
        questsTable.row()
        for ((quest, isDone) in questStatus) {
            val questText = createQuestToolTipText(quest)
            questsTable.add(scene2d.label(quest.name){
                addListener(TextTooltip(questText, Scene2DSkin.defaultSkin).also {
                    it.setInstant(true)
                })
            }).growX()
            questsTable.add(if(isDone) "Yes" else "False")
            questsTable.row()
        }
    }

    private fun createQuestToolTipText(quest: Quest): String? {
        return "Quest ${quest.name}\n" + "task:${quest.task}" + "reward:${quest.reward}\n"
    }

    fun updateHeroInventory(inventory: List<ItemInstance>) {
        inventoryTable.clearChildren()
        inventoryTable.add("Name").growX()
        inventoryTable.add("Qty")
        inventoryTable.add("Price")
        inventoryTable.row()
        val itemNames = inventory.map { itemInstance -> itemInstance.item }
        val count = itemNames.groupingBy { it.name to it.price }.eachCount()
        for ((pair, qty) in count) {
            val itemName = pair.first
            val price = pair.second
            inventoryTable.add(itemName).growX()
            inventoryTable.add(qty.toString())
            inventoryTable.add(price.toString())
            inventoryTable.row()
        }
        updateHeroWeapons(inventory.filter { itemInstance -> itemInstance.item.has(DamageItemComponent::class) })
        updateHeroConsumables(inventory.filter { itemInstance -> itemInstance.item.has(RecoverHealthItemComponent::class) })

        tradeWindow.updateHeroInventory(inventory)
    }

    private fun updateHeroConsumables(inventory: List<ItemInstance>) {
        val items = inventory.filter { itemInstance -> itemInstance.item.has(RecoverHealthItemComponent::class) }.map { it.item.name }.toTypedArray()
        heroConsumablesSelectBox.setItems("None", *items)
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
        monsterHPLbl.txt = monsterInstance.health.hitPoints.toString()
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

    fun updateTrader(trader: Trader) {
        tradeButton.isVisible = true
        tradeWindow.updateTraderName(trader.name)
        val traderInventory = trader.inventory
        tradeWindow.updateTraderInventory(traderInventory.itemInstances)
    }

    fun removeTrader() {
        tradeButton.isVisible = false
        tradeWindow.updateTraderInventory(listOf())
    }

    fun updateHeroRecipes(recipes: MutableList<Recipe>) {
        recipesTable.clearChildren()
        recipesTable.add("Name").growX()
        recipesTable.row()
        for (recipe in recipes) {
            recipesTable.add(recipe.name).growX()
            recipesTable.add(scene2d.textButton("Craft"){
                onClick {
                    gamePresenter.craftItem(recipe)
                }
            })
            recipesTable.row()
        }
    }


}
