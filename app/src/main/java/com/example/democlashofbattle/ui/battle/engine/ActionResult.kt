package com.example.democlashofbattle.ui.battle.engine

import com.example.democlashofbattle.models.Capability

data class ActionResult(
    val damage: Int = 0,
    val defense: Int = 0,
    val heal: Int = 0,
    val usedCapability: Capability? = null,
)