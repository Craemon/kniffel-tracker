package service

import data.GameSheetDao
import models.GameSheet

class GameSheetService(private val gameSheetDao: GameSheetDao) {
    fun createGameSheet(gameSheet: GameSheet): GameSheet {
        return gameSheetDao.create(gameSheet)
    }

    fun getGameSheet(id: Int): GameSheet {
        return gameSheetDao.get(id)
    }

    fun deleteGameSheet(id: Int): Boolean {
        return gameSheetDao.delete(id)
    }

}
