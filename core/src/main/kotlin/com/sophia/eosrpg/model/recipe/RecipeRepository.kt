package com.sophia.eosrpg.model.recipe


class RecipeRepository {
    val recipes = mutableListOf<Recipe>()

    fun save(item : Recipe){
        recipes.add(item)
    }

    fun findByName(name: String): Recipe {
        return recipes.first { item -> item.name == name }
    }

    fun saveAll(items: List<Recipe>) {
        this.recipes.addAll(items)
    }

}
