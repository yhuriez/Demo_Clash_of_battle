package com.example.democlashofbattle.ui.battle.engine

import com.example.democlashofbattle.models.Capability

data class PlayerBattleInfo(
    val remainingCapabilities: List<Capability>,
    val pv: Int = 50
)