package com.sophia.eosrpg.model.recipe

import com.sophia.eosrpg.model.item.Item

class Recipe(
    val name : String,
    val ingredients : Map<Item, Int>,
    val outputItems : Map<Item, Int>,
) {

}
