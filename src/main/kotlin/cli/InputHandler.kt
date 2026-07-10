package cli

import service.PlayerService
import service.GameService
import service.GameSheetService
import service.ScoreCalculatorService

class InputHandler(
    private val playerService: PlayerService,
    private val gameService: GameService,
    private val gameSheetService: GameSheetService,
    private val scoreCalculatorService: ScoreCalculatorService
) {
    fun start() {
        while (true) {
            println("\n1. Player Management")
            println("2. Create New Game")
            println("3. View Game Results")
            println("4. List All Games")
            println("5. Delete Game")
            println("6. Exit")

            when (readln().trim()) {
                "1" -> playerManagementMenu()
                "2" -> createNewGame()
                "3" -> viewGameResults()
                "4" -> listAllGames()
                "5" -> deleteGame()
                "6" -> return
                else -> println("Invalid input")
            }
        }
    }

    private fun playerManagementMenu() {
        while (true) {
            println("\n1. Create player")
            println("2. List players")
            println("3. Delete player")
            println("4. Back")

            when (readln().trim()) {
                "1" -> createPlayer()
                "2" -> listPlayers()
                "3" -> deletePlayer()
                "4" -> return
                else -> println("Invalid input")
            }
        }
    }

    private fun createNewGame() {
        try {
            val players = playerService.getAllPlayers()
            if (players.isEmpty()) {
                println("No players available!")
                return
            }

            println("\nAvailable players:")
            players.forEach { println("${it.id}: ${it.name}") }

            val selectedPlayerIds = mutableListOf<Int>()
            println("\nEnter player IDs (comma-separated):")
            val input = readln().trim()

            if (input.isEmpty()) {
                println("No input provided")
                return
            }

            try {
                val ids = input.split(",").map { it.trim().toInt() }
                for (id in ids) {
                    if (players.any { it.id == id }) {
                        selectedPlayerIds.add(id)
                    } else {
                        println("Player $id not found")
                    }
                }
            } catch (e: NumberFormatException) {
                println("Invalid input. Please enter valid numbers.")
                return
            }

            if (selectedPlayerIds.isEmpty()) {
                println("No valid players selected")
                return
            }

            val game = gameService.create()
            println("Game created with ID: ${game.id}")

            for (playerId in selectedPlayerIds) {
                try {
                    val player = playerService.getPlayer(playerId)
                    println("\n--- Enter scores for ${player.name} ---")
                    inputGameScores(game.id!!, playerId)
                } catch (e: NoSuchElementException) {
                    println("Player $playerId not found, skipping...")
                }
            }

            println("\nGame saved!")

        } catch (e: Exception) {
            println("Error creating game: ${e.message}")
        }
    }

    private fun inputGameScores(gameId: Int, playerId: Int) {
        val gameSheet = models.GameSheet(gameId = gameId, playerId = playerId)

        println("Ones:")
        gameSheet.ones = readln().trim().toIntOrNull() ?: 0
        println("Twos:")
        gameSheet.twos = readln().trim().toIntOrNull() ?: 0
        println("Threes:")
        gameSheet.threes = readln().trim().toIntOrNull() ?: 0
        println("Fours:")
        gameSheet.fours = readln().trim().toIntOrNull() ?: 0
        println("Fives:")
        gameSheet.fives = readln().trim().toIntOrNull() ?: 0
        println("Sixes:")
        gameSheet.sixes = readln().trim().toIntOrNull() ?: 0
        println("Three of a Kind:")
        gameSheet.threeOfAKind = readln().trim().toIntOrNull() ?: 0
        println("Four of a Kind:")
        gameSheet.fourOfAKind = readln().trim().toIntOrNull() ?: 0
        println("Full House (0 or 25):")
        gameSheet.fullHouse = readln().trim().toIntOrNull() ?: 0
        println("Small Straight (0 or 30):")
        gameSheet.smallStraight = readln().trim().toIntOrNull() ?: 0
        println("Large Straight (0 or 40):")
        gameSheet.largeStraight = readln().trim().toIntOrNull() ?: 0
        println("Kniffel (0 or 50):")
        gameSheet.kniffel = readln().trim().toIntOrNull() ?: 0
        println("Chance:")
        gameSheet.chance = readln().trim().toIntOrNull() ?: 0
        println("Kniffel Bonus:")
        gameSheet.kniffelBonus = readln().trim().toIntOrNull() ?: 0

        try {
            val saved = gameSheetService.createGameSheet(gameSheet)
            val total = scoreCalculatorService.calculateGrandTotal(saved)
            println("Total: $total")
        } catch (e: Exception) {
            println("Error saving scores: ${e.message}")
        }
    }

    private fun viewGameResults() {
        println("Enter game ID:")
        val gameIdInput = readln().trim()

        if (gameIdInput.isEmpty()) {
            println("No input provided")
            return
        }

        val gameId = try {
            gameIdInput.toInt()
        } catch (e: NumberFormatException) {
            println("Invalid ID. Please enter a number.")
            return
        }

        try {
            val game = gameService.get(gameId)
            val sheets = gameService.getGameSheetsByGameId(gameId)

            if (sheets.isEmpty()) {
                println("No results for this game")
                return
            }

            println("\nGame #${game.id} - ${game.createdAt}")
            val results = sheets.map { sheet ->
                val total = scoreCalculatorService.calculateGrandTotal(sheet)
                Pair(sheet, total)
            }.sortedByDescending { it.second }

            results.forEachIndexed { index, (sheet, total) ->
                try {
                    val player = playerService.getPlayer(sheet.playerId)
                    println("${index + 1}. ${player.name}: $total")
                } catch (e: NoSuchElementException) {
                    println("${index + 1}. Unknown player (ID: ${sheet.playerId}): $total")
                }
            }

            println("\n1. View detailed gamesheet")
            println("2. Back to menu")
            when (readln().trim()) {
                "1" -> displayGameSheet(gameId)
                else -> return
            }

        } catch (e: NoSuchElementException) {
            println("Game not found.")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun createPlayer() {
        println("Enter player name:")
        val name = readln().trim()

        if (name.isEmpty()) {
            println("Name cannot be empty.")
            return
        }

        try {
            val player = playerService.createPlayer(name)
            println("Created: ${player.name} (ID: ${player.id})")
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun deletePlayer() {
        println("Enter player id:")
        val idInput = readln().trim()

        if (idInput.isEmpty()) {
            println("No input provided")
            return
        }

        val id = try {
            idInput.toInt()
        } catch (e: NumberFormatException) {
            println("Invalid ID. Please enter a number.")
            return
        }

        try {
            playerService.deletePlayer(id)
            println("Deleted")
        } catch (e: NoSuchElementException) {
            println("Player not found.")
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

        println("\n=== Players ===")
        players.forEach { println("${it.id}: ${it.name}") }
    }

    private fun listAllGames() {
        try {
            val games = gameService.getAll()

            if (games.isEmpty()) {
                println("No games found")
                return
            }

            println("\n=== All Games ===")
            games.forEach { game ->
                try {
                    val sheets = gameService.getGameSheetsByGameId(game.id!!)
                    val playerCount = sheets.size
                    println("Game #${game.id} - ${game.createdAt} ($playerCount players)")
                } catch (e: Exception) {
                    println("Game #${game.id} - ${game.createdAt} (error loading players)")
                }
            }

        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun deleteGame() {
        println("Enter game ID:")
        val gameIdInput = readln().trim()

        if (gameIdInput.isEmpty()) {
            println("No input provided")
            return
        }

        val gameId = try {
            gameIdInput.toInt()
        } catch (e: NumberFormatException) {
            println("Invalid ID. Please enter a number.")
            return
        }

        try {
            val deleted = gameService.deleteGame(gameId)
            if (deleted) {
                println("Game #$gameId deleted")
            } else {
                println("Failed to delete game")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun displayGameSheet(gameId: Int) {
        try {
            val sheets = gameService.getGameSheetsByGameId(gameId)

            if (sheets.isEmpty()) {
                println("No sheets for this game")
                return
            }

            println("\n=== Detailed Gamesheet ===")
            sheets.forEach { sheet ->
                try {
                    val player = playerService.getPlayer(sheet.playerId)
                    val total = scoreCalculatorService.calculateGrandTotal(sheet)

                    println("\n${player.name}:")
                    println("  Upper Section:")
                    println("    Ones:        ${sheet.ones}")
                    println("    Twos:        ${sheet.twos}")
                    println("    Threes:      ${sheet.threes}")
                    println("    Fours:       ${sheet.fours}")
                    println("    Fives:       ${sheet.fives}")
                    println("    Sixes:       ${sheet.sixes}")
                    println("  Lower Section:")
                    println("    3 of a Kind: ${sheet.threeOfAKind}")
                    println("    4 of a Kind: ${sheet.fourOfAKind}")
                    println("    Full House:  ${sheet.fullHouse}")
                    println("    Small Str:   ${sheet.smallStraight}")
                    println("    Large Str:   ${sheet.largeStraight}")
                    println("    Kniffel:     ${sheet.kniffel}")
                    println("    Chance:      ${sheet.chance}")
                    println("    Kniffel Bonus: ${sheet.kniffelBonus}")
                    println("  ─────────────")
                    println("  Total:       $total")
                } catch (e: Exception) {
                    println("Error displaying sheet: ${e.message}")
                }
            }

        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
}