package com.sophia.eosrpg.screen

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.sophia.eosrpg.model.item.ItemInstance
import com.sophia.eosrpg.presenter.GamePresenter
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
        titleTable.add(scene2d.textButton("X"){
            onClick {
                this@TradeWindow.remove()
            }
        }).right()
        val table = scene2d.table {
            this.defaults().pad(10f)
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
                background = skin.getDrawable("white")
                heroInventoryTable = this
            }
            table {
                it.grow()
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

    fun updateHeroInventory(inventory : List<ItemInstance> ){
        heroInventoryTable.clearChildren()
        heroInventoryTable.add("Description")
        heroInventoryTable.add("Price")
        heroInventoryTable.add()
        heroInventoryTable.row()
        for (itemInstance in inventory){
            heroInventoryTable.add(itemInstance.item.name)
            heroInventoryTable.add(itemInstance.item.price.toString())
            heroInventoryTable.add(scene2d.textButton("Sell"){
                onClick {
                    gamePresenter.heroSellItemInstance(itemInstance)
                }
            })
            heroInventoryTable.row()
        }
        pack()
    }

    fun updateTraderInventory(inventory : List<ItemInstance> ){
        traderInventoryTable.clearChildren()
        traderInventoryTable.add("Description")
        traderInventoryTable.add("Price")
        traderInventoryTable.add()
        traderInventoryTable.row()
        for (itemInstance in inventory){
            traderInventoryTable.add(itemInstance.item.name)
            traderInventoryTable.add(itemInstance.item.price.toString())
            traderInventoryTable.add(scene2d.textButton("Buy"){
                onClick {
                    gamePresenter.heroBuyItemInstance(itemInstance)
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
