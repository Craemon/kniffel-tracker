package service

import data.GameDao
import data.GameSheetDao
import models.Game
import models.GameSheet

class GameService(private val gameDao: GameDao, private val gameSheetDao: GameSheetDao, private val gameSheetService: GameSheetService) {
    fun create(): Game {
        return gameDao.create()
    }

    fun delete(id: Int): Boolean {
        return gameDao.delete(id)
    }

    fun deleteGame(id: Int): Boolean {
        val sheets = getGameSheetsByGameId(id)
        for (sheet in sheets) {
            try {
                gameSheetService.deleteGameSheet(sheet.id!!)
            } catch (e: Exception) {
                return false
            }
        }
        return gameDao.delete(id)
    }

    fun get(id: Int): Game {
        return gameDao.get(id)
    }

    fun getAll(): List<Game> {
        return gameDao.getAll()
    }

    fun getGameSheetsByGameId(gameId: Int): List<GameSheet> {
        return gameSheetDao.getByGameId(gameId)
    }
}