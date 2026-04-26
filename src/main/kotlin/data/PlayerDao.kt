package data

import Database
import models.Player
import java.sql.Statement

class PlayerDao(private val db: Database) {
    fun create(name: String): Player {
        val conn = db.connect()

        val stmt = conn.prepareStatement(
            "INSERT INTO players (name) VALUES (?)",
            Statement.RETURN_GENERATED_KEYS
        )

        stmt.setString(1, name)
        stmt.executeUpdate()

        val rs = stmt.generatedKeys
        val id = if (rs.next()) rs.getInt(1) else null

        return Player(id, name)
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