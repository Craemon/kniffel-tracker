import cli.InputHandler
import data.PlayerDao
import data.GameDao
import data.GameSheetDao
import service.PlayerService
import service.GameService
import service.GameSheetService
import service.ScoreCalculatorService

fun main() {
    val db = Database()

    val playerDao = PlayerDao(db)
    val gameDao = GameDao(db)
    val gameSheetDao = GameSheetDao(db)

    val playerService = PlayerService(playerDao)
    val gameSheetService = GameSheetService(gameSheetDao)
    val scoreCalculatorService = ScoreCalculatorService()
    val gameService = GameService(gameDao, gameSheetDao, gameSheetService)

    val inputHandler = InputHandler(playerService, gameService, gameSheetService, scoreCalculatorService)
    inputHandler.start()
}