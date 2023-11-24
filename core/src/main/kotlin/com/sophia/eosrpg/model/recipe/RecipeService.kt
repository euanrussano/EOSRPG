package com.sophia.eosrpg.model.recipe

import com.sophia.eosrpg.model.Hero
import com.sophia.eosrpg.model.item.ItemInstance
import com.sophia.eosrpg.model.item.ItemInstanceFactory

class RecipeService(
    val recipeRepository: RecipeRepository,
    val itemInstanceFactory: ItemInstanceFactory
) {
    fun craftItem(hero: Hero, recipe: Recipe) : Boolean{
        val heroInventory = hero.inventory
        val itemQuantity = recipe.ingredients.map {entry -> entry.key.name to entry.value }.toMap()
        if (heroInventory.hasAllItems(itemQuantity)){
            heroInventory.removeItems(recipe.ingredients)
            for ((item, qty) in recipe.outputItems) {
                for (i in 0 until qty){
                    val itemInstance = ItemInstance(item)
                    heroInventory.addItemInstanceToInventory(itemInstance)
                }
            }
            return true
        }
        return false
    }

}
