package com.sophia.eosrpg.model.skill

class SkillRepository {
    val skills = mutableListOf<Skill>()

    fun save(skill: Skill) {
        skills.add(skill)
    }

    fun findByName(name: String): Skill {
        return skills.first { skill -> skill.name == name }
    }

    fun saveAll(vararg skill: Skill) {
        this.skills.addAll(skill)
    }
}
