package service

import data.PlayerDao
import models.Player

class PlayerService(private val playerDao: PlayerDao) {
    fun createPlayer(name: String): Player {
        require(name.isNotEmpty()) { "Name cannot be empty" }
        return playerDao.create(name)
    }

    fun getAllPlayers(): List<Player> {
        return playerDao.getAll()
    }
}