package com.example.democlashofbattle.ui.battle.engine

interface RandomGenerator {

    fun getD6() : Int

    fun getRiskyD6() : Int

    fun getDoubleD6() : Int

    fun getPreciseD6() : Int

    fun getRandomAction(remainingCapabilitiesCount: Int) : Int
}