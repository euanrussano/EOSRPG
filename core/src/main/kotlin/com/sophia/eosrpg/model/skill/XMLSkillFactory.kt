package com.sophia.eosrpg.model.skill

import com.badlogic.gdx.utils.XmlReader
import ktx.assets.toInternalFile

class XMLSkillFactory(
    val skillRepository: SkillRepository
) {
    val xmlReader  = XmlReader()
    val xmlFile = "skill/skill.xml".toInternalFile()

    init {
        val skills = readXML()
        skillRepository.saveAll(*skills.toTypedArray())
    }

    private fun readXML(): List<Skill> {
        val skills = mutableListOf<Skill>()

        val skillRoot = xmlReader.parse(xmlFile)
        for (idx in 0 until skillRoot.childCount) {
            val skillElement = skillRoot.getChild(idx)
            val name = skillElement.getChildByName("name").text
            val attribute = skillElement.getChildByName("baseAttribute").text
            val modifier = skillElement.getInt("modifier")
            val skill = Skill(name, attribute, modifier)
            skills.add(skill)
        }

        return skills
    }
}
