package hu.bme.nandiii.gamebrowser.data.network

interface GameRepository {
    suspend fun saveGame(gameId: String)
    suspend fun getGame(gameID: String): String?
    suspend fun deleteGame(gameID: String)
    suspend fun getAllGames(): List<String>
}