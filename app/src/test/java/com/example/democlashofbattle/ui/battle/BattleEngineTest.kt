package com.example.democlashofbattle.ui.battle

import com.example.democlashofbattle.models.Capability
import com.example.democlashofbattle.ui.battle.engine.ActionResult
import com.example.democlashofbattle.ui.battle.engine.BattleEngine
import com.example.democlashofbattle.ui.battle.engine.PlayerBattleInfo
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class BattleEngineTest {

    private lateinit var myPlayerBattleInfo: PlayerBattleInfo

    private lateinit var opponentBattleInfo: PlayerBattleInfo


    @Test
    fun check_simple_attack_vs_simple_attack() {
        // Given
        myPlayerBattleInfo = PlayerBattleInfo(remainingCapabilities = listOf(
            Capability.RISKY_ATTACK,
            Capability.DOUBLE_ATTACK,
            Capability.PRECISE_ATTACK,
        ))
        opponentBattleInfo = PlayerBattleInfo(remainingCapabilities = listOf(
            Capability.RISKY_ATTACK,
            Capability.DOUBLE_ATTACK,
            Capability.PRECISE_ATTACK,
        ))
        val engine = BattleEngine(MockRandomGenerator(d6Value = 3, ))

        // When
        val result = engine.attack(opponentBattleInfo, null)

        // Then
        assertEquals(result.first, ActionResult(damage = 3))
        assertEquals(result.first.usedCapability, null)
        assertEquals(result.second, ActionResult(damage = 3))
        assertEquals(result.second.usedCapability, null)
    }

    @Test
    fun check_risky_attack_vs_simple_attack() {
        // Given
        myPlayerBattleInfo = PlayerBattleInfo(remainingCapabilities = listOf(
            Capability.RISKY_ATTACK,
            Capability.DOUBLE_ATTACK,
            Capability.PRECISE_ATTACK,
        ))
        opponentBattleInfo = PlayerBattleInfo(remainingCapabilities = listOf(
            Capability.RISKY_ATTACK,
            Capability.DOUBLE_ATTACK,
            Capability.PRECISE_ATTACK,
        ))
        val engine = BattleEngine(MockRandomGenerator(d6Value = 3, ))

        // When
        val result = engine.attack(opponentBattleInfo, null)

        // Then
        assertEquals(result.first, ActionResult(damage = 3))
        assertEquals(result.first.usedCapability, null)
        assertEquals(result.second, ActionResult(damage = 3))
        assertEquals(result.second.usedCapability, null)
    }
}