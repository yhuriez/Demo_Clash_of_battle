package com.example.democlashofbattle.ui.battle.engine

import android.content.Context
import com.example.democlashofbattle.models.Capability
import com.example.democlashofbattle.models.CapabilityType
import com.example.democlashofbattle.models.Player
import com.example.democlashofbattle.utils.getNameId


fun getTextForActionResult(context: Context, player: Player, actionResult: ActionResult) : String {
    val usedCapability = actionResult.usedCapability

    return if(usedCapability != null) {
        val capabilityName = context.getString(usedCapability.getNameId())
        val capabilitySuffix = getCapabilitySuffix(usedCapability, actionResult)

        "${player.name} utilise $capabilityName $capabilitySuffix"

    } else {
        "${player.name} fait une attaque simple et inflige ${actionResult.damage}"
    }
}

fun getCapabilitySuffix(usedCapability: Capability, result: ActionResult) = when(usedCapability.type){
    CapabilityType.ATTACK -> "et inflige ${result.damage} dégâts"
    CapabilityType.DEFENSE -> ", inflige ${result.damage} dégâts et annule ${result.defense} dégâts"
    CapabilityType.HEAL -> "et se soigne de ${result.heal} PV"
}
