package com.sophia.eosrpg.model.recipe

import com.badlogic.gdx.utils.XmlReader
import com.sophia.eosrpg.model.item.Item
import com.sophia.eosrpg.model.item.ItemRepository
import ktx.assets.toInternalFile

class XMLRecipeFactory(
    val recipeRepository: RecipeRepository,
    val itemRepository: ItemRepository
) {

    val xmlReader  = XmlReader()
    val xmlFile = "recipe/recipe.xml".toInternalFile()

    init {
        val recipes = readXML()

        for (recipe in recipes) {
            recipeRepository.save(recipe)
        }

    }

    private fun readXML(): List<Recipe> {
        val recipes = mutableListOf<Recipe>()

        val recipesRoot = xmlReader.parse(xmlFile)
        for (idx in 0 until recipesRoot.childCount){
            val recipeElement = recipesRoot.getChild(idx)
            val name = recipeElement.getChildByName("name").text

            val ingredients = mutableMapOf<Item, Int>()

            val ingredientsRoot = recipeElement.getChildByName("ingredients")
            for (idx1 in 0 until ingredientsRoot.childCount){
                val ingredientElement = ingredientsRoot.getChild(idx1)
                val itemName = ingredientElement.getAttribute("name")
                val itemQty = ingredientElement.getAttribute("quantity").toInt()

                val item = itemRepository.findByName(itemName)
                ingredients[item] = itemQty
            }

            val outputs = mutableMapOf<Item, Int>()

            val outputsRoot = recipeElement.getChildByName("outputs")
            for (idx1 in 0 until outputsRoot.childCount){
                val outputElement = outputsRoot.getChildByName("item")
                val itemName = outputElement.getAttribute("name")
                val itemQty = outputElement.getAttribute("quantity").toInt()

                val item = itemRepository.findByName(itemName)
                outputs[item] = itemQty
            }

            val recipe = Recipe(name, ingredients, outputs)
            recipes.add(recipe)
        }
        return recipes
    }

}
