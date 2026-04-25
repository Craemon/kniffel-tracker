import java.sql.Connection
import java.sql.DriverManager

class Database {
    private var connection: Connection? = null

    fun connect(): Connection {
        if (connection == null || connection!!.isClosed) {
            val dbPath = "kniffel.db"
            connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
            connection!!.createStatement().execute("PRAGMA foreign_keys=ON;")
            createTables();
        }
        return connection!!
    }

    private fun createTables() {
        val schema = javaClass.getResourceAsStream("/schema.sql")?.bufferedReader()?.readText()?: throw RuntimeException("Could not load database schema!")

        val statements = schema.split(";")

        connection?.createStatement()?.use { stmt ->
            for (statement in statements) {
                val trimmed = statement.trim()
                if (trimmed.isNotEmpty()) {
                    stmt.execute(trimmed)
                }
            }
        }
    }

    fun close() {
        connection?.close()
        connection = null
    }
}