package service

import data.GameDao
import models.Game

class GameService(private val gameDao: GameDao) {
    fun create(): Game {
        return gameDao.create()
    }

    fun delete(id: Int): Boolean {
        return gameDao.delete(id)
    }

    fun get(id: Int): Game {
        return gameDao.get(id)
    }

    fun getAll(): List<Game> {
        return gameDao.getAll()
    }
}