package com.sophia.eosrpg.screen

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.sophia.eosrpg.model.item.ItemInstance
import com.sophia.eosrpg.presenter.GamePresenter
import ktx.actors.centerPosition
import ktx.actors.onClick
import ktx.actors.txt
import ktx.scene2d.*

class TradeWindow(val gamePresenter: GamePresenter) : Window("Trade", Scene2DSkin.defaultSkin) {

    private val traderNameLbl: Label
    private val heroInventoryTable: Table
    private val traderInventoryTable: Table

    init {
        isModal = true
        isMovable = true
        background = skin.newDrawable("white", Color.LIGHT_GRAY)
        titleTable.add(scene2d.textButton("X"){
            onClick {
                this@TradeWindow.remove()
            }
        }).right()
        val table = scene2d.table {
            this.defaults().pad(10f).top().left()
            label("<Trader Name>"){
                it.colspan(2)
                traderNameLbl = this
            }
            row()
            label("Your Inventory")
            label("Trader Inventory")
            row()
            table {
                it.grow()
                this.defaults().pad(10f)
                background = skin.getDrawable("white")
                heroInventoryTable = this
            }
            table {
                it.grow()
                this.defaults().pad(10f)
                background = skin.getDrawable("white")
                traderInventoryTable = this
            }
            row()
            add()
            textButton("Close"){
                onClick {
                    this@TradeWindow.remove()
                }
            }
        }

        padTop(20f)
        add(table).grow()
        pack()
    }

    override fun setStage(stage: Stage?) {
        super.setStage(stage)
        stage?.let {
            centerPosition(stage.width ,stage.height)
        }
    }

    fun updateHeroInventory(inventory : List<ItemInstance> ){
        heroInventoryTable.clearChildren()
        heroInventoryTable.add("Description")
        heroInventoryTable.add("Qty")
        heroInventoryTable.add("Price")
        heroInventoryTable.add()
        heroInventoryTable.row()
        val count = inventory.groupBy { it.item }.toSortedMap(compareBy{ it.name })
        for ((item, itemInstances) in count) {
            val itemName = item.name
            val price = item.price
            heroInventoryTable.add(itemName)
            heroInventoryTable.add(itemInstances.size.toString())
            heroInventoryTable.add(price.toString())
            heroInventoryTable.add(scene2d.textButton("Sell 1"){
                this.pad(5f)
                onClick {
                    gamePresenter.heroSellItemInstance(itemInstances.first())
                }
            })
            heroInventoryTable.row()
        }
        pack()
    }

    fun updateTraderInventory(inventory : List<ItemInstance> ){
        traderInventoryTable.clearChildren()
        traderInventoryTable.add("Description")
        traderInventoryTable.add("Qty")
        traderInventoryTable.add("Price")
        traderInventoryTable.add()
        traderInventoryTable.row()
        val count = inventory.groupBy { it.item }.toSortedMap(compareBy{ it.name })
        for ((item, itemInstances) in count) {
            val itemName = item.name
            val price = item.price
            traderInventoryTable.add(itemName)
            traderInventoryTable.add(itemInstances.size.toString())
            traderInventoryTable.add(price.toString())
            traderInventoryTable.add(scene2d.textButton("Buy 1"){
                this.pad(5f)
                onClick {
                    gamePresenter.heroBuyItemInstance(itemInstances.first())
                }
            })
            traderInventoryTable.row()
        }
        pack()
    }

    fun updateTraderName(name : String){
        traderNameLbl.txt = name
    }
}
