package cli

import models.Player
import service.PlayerService

class InputHandler(private val playerService: PlayerService) {
    fun start() {
        while (true) {
            println("\n1. Create player")
            println("2. List players")
            println("3. Find player")
            println("4. Delete player")
            println("5. Exit")

            when (readln()) {
                "1" -> createPlayer()
                "2" -> listPlayers()
                "3" -> getPlayer()
                "4" -> deletePlayer()
                "5" -> return
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

    private fun getPlayer() {
        println("Enter player id: ")
        val id = readln().toInt()

        try {
            val player = playerService.getPlayer(id)
            println("Found player with id: ${player.id} and name: ${player.name}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun deletePlayer() {
        println("Enter player id: ")
        val id = readln().toInt()

        try {
            playerService.deletePlayer(id)
            println("Deleted player with id: $id")
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