package com.example.democlashofbattle.ui.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.democlashofbattle.api.PlayerApi
import com.example.democlashofbattle.database.AppDatabase
import com.example.democlashofbattle.database.PlayerDao
import com.example.democlashofbattle.models.Capability
import com.example.democlashofbattle.models.Player
import com.example.democlashofbattle.utils.MY_PLAYER_NAME
import kotlinx.coroutines.launch

class EditPlayerViewModel : ViewModel() {

    // Dépendances
    private val playerDao: PlayerDao by lazy { AppDatabase.instance!!.playerDao() }

    private val playerApi: PlayerApi by lazy { PlayerApi.service }


    val myPlayer = MutableLiveData<Player>()

    init {
        viewModelScope.launch {
            myPlayer.value = playerDao.getByRemoteId(MY_PLAYER_NAME)
        }
    }

    fun updateCapability(index: Int, capability: Capability?) {
        capability?.let {
            val player = checkNotNull(myPlayer.value)

            myPlayer.value = when(index) {
                0 -> player.copy(capability1 = capability)
                1 -> player.copy(capability2 = capability)
                2 -> player.copy(capability3 = capability)
                else -> throw IllegalStateException("No capability for index $index")
            }
        }
    }

    suspend fun validate(name: String, imageUrl: String) {
        myPlayer.value?.let { player ->

            val modifiedPlayer = player.copy(
                name = name,
                imageUrl = imageUrl
            )

            val remoteId = checkNotNull(modifiedPlayer.remoteId)
            playerApi.updatePlayer(remoteId, modifiedPlayer)

            playerDao.update(modifiedPlayer)
        }
    }
}