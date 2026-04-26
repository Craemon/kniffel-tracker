import cli.InputHandler
import data.PlayerDao
import service.PlayerService

fun main() {
    val db = Database()

    val playerDao = PlayerDao(db)
    val playerService = PlayerService(playerDao)

    val inputHandler = InputHandler(playerService)
    inputHandler.start()
}