package data

import Database
import models.GameSheet
import java.sql.SQLException
import java.sql.Statement

class GameSheetDao(private val db: Database) {
    fun create(gameSheet: GameSheet): GameSheet {
        db.connect().use { conn ->
            conn.prepareStatement(
                "INSERT INTO game_sheets (game_id, player_id, ones, twos, threes, fours, fives, sixes, three_of_a_kind, four_of_a_kind, full_house, small_straight, large_straight, kniffel, chance, kniffel_bonus) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
            ).use { stmt ->
                stmt.setInt(1, gameSheet.gameId)
                stmt.setInt(2, gameSheet.playerId)
                stmt.setInt(3, gameSheet.ones ?: 0)
                stmt.setInt(4, gameSheet.twos ?: 0)
                stmt.setInt(5, gameSheet.threes ?: 0)
                stmt.setInt(6, gameSheet.fours ?: 0)
                stmt.setInt(7, gameSheet.fives ?: 0)
                stmt.setInt(8, gameSheet.sixes ?: 0)
                stmt.setInt(9, gameSheet.threeOfAKind ?: 0)
                stmt.setInt(10, gameSheet.fourOfAKind ?: 0)
                stmt.setInt(11, gameSheet.fullHouse ?: 0)
                stmt.setInt(12, gameSheet.smallStraight ?: 0)
                stmt.setInt(13, gameSheet.largeStraight ?: 0)
                stmt.setInt(14, gameSheet.kniffel ?: 0)
                stmt.setInt(15, gameSheet.chance ?: 0)
                stmt.setInt(16, gameSheet.kniffelBonus ?: 0)
                stmt.executeUpdate()
                stmt.generatedKeys.use { rs ->
                    val id = if (rs.next()) {
                        rs.getInt("id")
                    } else {
                        throw SQLException("Could not create new GameSheet!")
                    }
                    return fetchGameSheetById(conn, id)
                }
            }
        }
    }

    fun delete(id: Int): Boolean {
        db.connect().use { conn ->
            conn.prepareStatement(
                "DELETE FROM game_sheets WHERE id =?"
            ).use { stmt ->
                stmt.setInt(1, id)
                return stmt.executeUpdate() > 0
            }
        }
    }

    fun get(id: Int): GameSheet {
        db.connect().use { conn ->
            return fetchGameSheetById(conn, id)
        }
    }

    private fun fetchGameSheetById(conn: java.sql.Connection, id: Int): GameSheet {
        conn.prepareStatement(
            "SELECT * FROM game_sheets WHERE id = ?"
        ).use { stmt ->
            stmt.setInt(1, id)
            stmt.executeQuery().use { rs ->
                if (rs.next()) {
                    return GameSheet(
                        id,
                        rs.getInt("game_id"),
                        rs.getInt("player_id"),
                        rs.getInt("ones"),
                        rs.getInt("twos"),
                        rs.getInt("threes"),
                        rs.getInt("fours"),
                        rs.getInt("fives"),
                        rs.getInt("sixes"),
                        rs.getInt("three_of_a_kind"),
                        rs.getInt("four_of_a_kind"),
                        rs.getInt("full_house"),
                        rs.getInt("small_straight"),
                        rs.getInt("large_straight"),
                        rs.getInt("kniffel"),
                        rs.getInt("chance"),
                        rs.getInt("kniffel_bonus")
                    )
                } else {
                    throw SQLException("GameSheet with id $id not found")
                }
            }
        }
    }
}