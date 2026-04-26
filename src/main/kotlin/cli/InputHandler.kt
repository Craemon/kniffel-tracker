package cli

import models.Player
import service.PlayerService

class InputHandler(private val playerService: PlayerService) {
    fun start() {
        while (true) {
            println("\n1. Create player")
            println("2. List players")
            println("3. Exit")

            when (readln()) {
                "1" -> createPlayer()
                "2" -> listPlayers()
                "3" -> return
                else -> println("Invalid input")
            }
        }
    }

    private fun createPlayer() {
        println("Enter player name: ")
        val name = readln()

        try {
            val player = playerService.createPlayer(name)
            println("Created player with id: ${player.id}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun listPlayers() {
        val players = playerService.getAllPlayers()

        if (players.isEmpty()) {
            println("No players")
            return
        }

        println("Players:")
        players.forEach {
            println("${it.id}: ${it.name}")
        }
    }
}