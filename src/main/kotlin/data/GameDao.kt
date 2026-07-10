package data

import Database
import models.Game
import java.sql.SQLException
import java.sql.Statement

class GameDao(private val db: Database) {
    fun create(): Game {
        db.connect().use { conn ->
            conn.prepareStatement(
                "INSERT INTO games DEFAULT VALUES",
                Statement.RETURN_GENERATED_KEYS
            ).use { stmt ->
                stmt.executeUpdate()
                stmt.generatedKeys.use { rs ->
                    val id = if (rs.next()) {
                        rs.getInt(1)
                    } else {
                        throw SQLException("Game could not be created")
                    }
                    return fetchGameById(conn, id)
                }
            }
        }
    }

    fun delete(id: Int): Boolean {
        db.connect().use { conn ->
            conn.prepareStatement(
                "DELETE FROM games WHERE id = ?"
            ).use { stmt ->
                stmt.setInt(1, id)
                return stmt.executeUpdate() > 0
            }
        }
    }

    fun get(id: Int): Game {
        db.connect().use { conn ->
            return fetchGameById(conn, id)
        }
    }

    fun getAll(): List<Game> {
        db.connect().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM games").use { rs ->
                    val games = mutableListOf<Game>()

                    while (rs.next()) {
                        games.add(
                            Game(
                                rs.getInt("id"),
                                rs.getString("created_at")
                            )
                        )
                    }
                    return games
                }
            }
        }
    }

    private fun fetchGameById(conn: java.sql.Connection, id: Int): Game {
        conn.prepareStatement(
            "SELECT created_at FROM games WHERE id = ?"
        ).use { stmt ->
            stmt.setInt(1, id)
            stmt.executeQuery().use { rs ->
                if (rs.next()) {
                    return Game(id, rs.getString("created_at"))
                } else {
                    throw NoSuchElementException("Game with id $id not found")
                }
            }
        }
    }
}