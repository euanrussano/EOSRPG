package com.sophia.eosrpg.model

open class Entity {

    val components = mutableListOf<EntityComponent>()

    inline fun <reified T: EntityComponent> get(): T {
        return components.first { entityComponent -> entityComponent is T } as T
    }

    fun add(component: EntityComponent) {
        components.add(component)
    }

}
