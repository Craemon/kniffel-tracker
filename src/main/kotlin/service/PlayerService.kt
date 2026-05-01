package service

import data.PlayerDao
import models.Player

class PlayerService(private val playerDao: PlayerDao) {
    fun createPlayer(name: String): Player {
        require(name.isNotEmpty()) { "Name cannot be empty" }
        require(name.length <= 25) { "Name cannot be longer than 25 characters" }
        return playerDao.create(name)
    }

    fun updatePlayer(id: Int, name: String): Player {
        require(name.isNotEmpty()) { "Name cannot be empty" }
        require(name.length <= 25) { "Name cannot be longer than 25 characters" }
        return playerDao.update(id, name)
    }

    fun deletePlayer(id: Int): Boolean {
         return playerDao.delete(id)
    }

    fun getPlayer(id: Int): Player {
        return playerDao.get(id)
    }

    fun getAllPlayers(): List<Player> {
        return playerDao.getAll()
    }
}