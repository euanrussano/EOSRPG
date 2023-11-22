package com.sophia.eosrpg.model.recipe

import com.sophia.eosrpg.model.item.Item
import com.sophia.eosrpg.model.item.ItemRepository

class RecipeFactory(
    val recipeRepository: RecipeRepository,
    val itemRepository: ItemRepository
) {

    init {
        val granolaBarRecipe = Recipe(
            "Granola Bar",
            mapOf(
                itemRepository.findByName("Oats") to 1,
                itemRepository.findByName("Honey") to 1,
                itemRepository.findByName("Raisins") to 1,
                ),
            mapOf(
                itemRepository.findByName("Granola Bar") to 1,
            )
        )


        recipeRepository.save(granolaBarRecipe)
    }

}
