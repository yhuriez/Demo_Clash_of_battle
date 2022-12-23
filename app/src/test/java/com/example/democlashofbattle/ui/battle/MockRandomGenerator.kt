package com.example.democlashofbattle.ui.battle

import com.example.democlashofbattle.ui.battle.engine.RandomGenerator

class MockRandomGenerator(
    private val d6Value: Int = 1,
    private val actionValue: Int = 1
) : RandomGenerator {

    override fun getD6() = d6Value

    override fun getRiskyD6() = getD6() * 2

    override fun getDoubleD6() = getD6() + getD6()

    override fun getPreciseD6() = getD6() + 3


    override fun getRandomAction(remainingCapabilitiesCount: Int) = actionValue
}