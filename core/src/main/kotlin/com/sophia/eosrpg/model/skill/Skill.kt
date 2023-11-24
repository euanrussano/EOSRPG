package com.sophia.eosrpg.model.skill

import com.sophia.eosrpg.model.entity.Entity

class Skill(
    val name : String,
    val baseAttribute : String,
    val modifier : Int
) {

    fun getPreDefinedValue(entity: Entity): Int{
        val attributeValue = when(baseAttribute){
            "DX" -> entity.DX
            "ST" -> entity.ST
            else -> throw Error("Error in ${this::class.simpleName}: Unknown attribute ${baseAttribute}")
        }
        return attributeValue + modifier
    }

}
