package com.example.democlashofbattle.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.democlashofbattle.api.PlayerApi
import com.example.democlashofbattle.database.AppDatabase
import com.example.democlashofbattle.database.PlayerDao
import com.example.democlashofbattle.models.Player
import com.example.democlashofbattle.utils.MY_PLAYER_NAME
import com.example.democlashofbattle.utils.toListOfPlayers
import kotlinx.coroutines.launch

class PlayerListViewModel : ViewModel() {

    // Dépendances
    val playerDao: PlayerDao by lazy { AppDatabase.instance!!.playerDao() }

    val playerApi: PlayerApi by lazy { PlayerApi.service }

    // LiveData
    val players = playerDao.watchAll()

    val myPlayer = playerDao.watchPlayer(MY_PLAYER_NAME)

    // Initialisation
    init {
        refresh()
    }

    // Récupére les joueurs provenant du serveur et remplace les joueurs en base de données
    fun refresh() {
        viewModelScope.launch {
            val players = playerApi.getPlayers().toListOfPlayers()

            playerDao.replaceAll(players)
        }
    }
}