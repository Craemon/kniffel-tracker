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

    fun delete(id: Int) {
        val conn = db.connect()

        val stmt = conn.prepareStatement(
            "DELETE FROM players WHERE id = ?",
        )
        stmt.setInt(1, id)
        stmt.executeUpdate()
    }

    fun get(id: Int): Player {
        val conn = db.connect()
        val stmt = conn.prepareStatement(
            "SELECT * FROM players WHERE id = ?",
        )
        stmt.setInt(1, id)
        val rs = stmt.executeQuery()

        return Player(rs.getInt(1), rs.getString(2))
    }

    fun getAll(): List<Player> {
        val conn = db.connect()
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM players")

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