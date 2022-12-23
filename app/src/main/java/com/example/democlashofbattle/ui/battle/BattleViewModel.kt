package com.example.democlashofbattle.ui.battle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.democlashofbattle.database.AppDatabase
import com.example.democlashofbattle.database.PlayerDao
import com.example.democlashofbattle.models.Capability
import com.example.democlashofbattle.models.Player
import com.example.democlashofbattle.ui.battle.engine.ActionResult
import com.example.democlashofbattle.ui.battle.engine.BattleEngine
import com.example.democlashofbattle.ui.battle.engine.PlayerBattleInfo
import com.example.democlashofbattle.ui.battle.engine.RandomGeneratorImpl
import com.example.democlashofbattle.utils.MY_PLAYER_NAME
import kotlinx.coroutines.launch
import java.lang.Integer.max

class BattleViewModel : ViewModel() {

    // Dépendances
    private val playerDao: PlayerDao by lazy { AppDatabase.instance!!.playerDao() }

    private val battleEngine = BattleEngine(RandomGeneratorImpl())

    // LiveData
    val myPlayerInfo = MutableLiveData<Player>()
    val opponentInfo = MutableLiveData<Player>()

    val myPlayerBattleInfo = MutableLiveData<PlayerBattleInfo>()
    val opponentBattleInfo = MutableLiveData<PlayerBattleInfo>()

    val rountCount = MutableLiveData(0)

    val lastPlayerResult = MutableLiveData<ActionResult>()
    val lastOpponentResult = MutableLiveData<ActionResult>()

    val winner = MutableLiveData<String>()


    fun init(opponentId: Long) {
        viewModelScope.launch {
            val myPlayer = playerDao.getByRemoteId(MY_PLAYER_NAME)
            val opponent = playerDao.get(opponentId)
            myPlayerInfo.value = myPlayer
            opponentInfo.value = opponent

            myPlayerBattleInfo.value = PlayerBattleInfo(
                remainingCapabilities = myPlayer.capabilities
            )
            opponentBattleInfo.value = PlayerBattleInfo(
                remainingCapabilities = opponent.capabilities
            )
        }
    }

    fun attack(capability: Capability? = null) {

        val player = myPlayerBattleInfo.value!!
        val opponent = opponentBattleInfo.value!!


        val result = battleEngine.attack(opponent, capability)


        val playerResult = result.first
        val opponentResult = result.second

        myPlayerBattleInfo.value = updatePlayer(player, playerResult, opponentResult)
        opponentBattleInfo.value = updatePlayer(opponent, opponentResult, playerResult)

        lastPlayerResult.value = playerResult
        lastOpponentResult.value = opponentResult

        rountCount.value = rountCount.value!! + 1

        if(myPlayerBattleInfo.value!!.pv <= 0) {
            winner.value = opponentInfo.value!!.name

        } else if(opponentBattleInfo.value!!.pv <= 0) {
            winner.value = myPlayerInfo.value!!.name
        }
    }


    private fun updatePlayer(
        player: PlayerBattleInfo,
        playerResult: ActionResult,
        opponentResult: ActionResult
    ): PlayerBattleInfo {
        var newPlayer = player

        val realDamage = max(0, opponentResult.damage - playerResult.defense)
        val heal = playerResult.heal

        playerResult.usedCapability?.let {
            val newCapabilitiesList = player.remainingCapabilities.toMutableList()
            newCapabilitiesList.remove(it)
            newPlayer = newPlayer.copy(remainingCapabilities = newCapabilitiesList)
        }

        val modifiedPv = newPlayer.pv - realDamage + heal
        val pv = Integer.min(50, Integer.max(0, modifiedPv))

        return newPlayer.copy(pv = pv)
    }
}
