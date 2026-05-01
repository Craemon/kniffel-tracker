package data

import Database
import models.Player
import java.sql.SQLException
import java.sql.Statement

class PlayerDao(private val db: Database) {
    fun create(name: String): Player {
        db.connect().use { conn ->
            conn.prepareStatement(
                "INSERT INTO players (name) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
            ).use { stmt ->
                stmt.setString(1, name)
                stmt.executeUpdate()
                stmt.generatedKeys.use { rs ->
                    val id = if (rs.next()) {
                        rs.getInt(1)
                    } else {
                        throw SQLException("Couldn't generate new player")
                    }
                    return Player(id, name)
                }
            }
        }
    }

    fun update(id: Int, newName: String): Player {
        db.connect().use { conn ->
            conn.prepareStatement(
                "UPDATE players SET name = ? WHERE id = ?",
            ).use { stmt ->
                stmt.setString(1, newName)
                stmt.setInt(2, id)
                val rowsAffected = stmt.executeUpdate()
                if (rowsAffected == 0) {
                    throw NoSuchElementException("Player with id $id not found")
                }
                return get(id)
            }
        }
    }

    fun delete(id: Int): Boolean {
        db.connect().use { conn ->
            conn.prepareStatement(
                "DELETE FROM players WHERE id = ?"
            ).use { stmt ->
                stmt.setInt(1, id)
                return stmt.executeUpdate() > 0
            }
        }
    }

    fun get(id: Int): Player {
        db.connect().use { conn ->
            conn.prepareStatement(
                "SELECT * FROM players WHERE id = ?"
            ).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        return Player(rs.getInt("id"), rs.getString("name"))
                    } else {
                        throw NoSuchElementException("Player with id $id not found")
                    }
                }
            }
        }
    }

    fun getAll(): List<Player> {
        db.connect().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM players").use { rs ->
                    val players = mutableListOf<Player>()

                    while (rs.next()) {
                        players.add(
                            Player(
                                id = rs.getInt("id"),
                                name = rs.getString("name")
                            )
                        )
                    }
                    return players
                }
            }
        }
    }
}