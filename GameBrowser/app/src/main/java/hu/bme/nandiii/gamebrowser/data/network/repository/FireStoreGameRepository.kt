package hu.bme.nandiii.gamebrowser.data.network.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import hu.bme.nandiii.gamebrowser.data.auth.AuthService
import hu.bme.nandiii.gamebrowser.data.network.GameRepository
import hu.bme.nandiii.gamebrowser.domain.game.FireStoreGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireStoreGameRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val authService: AuthService,
) : GameRepository {

    val games: Flow<List<FireStoreGame>> = authService.currentUser.flatMapLatest { user ->
        if (user == null) flow { emit(emptyList()) }
        else currentCollection(user.id).snapshots()
            .map { snapshot -> snapshot.toObjects(FireStoreGame::class.java) }
    }

    override suspend fun saveGame(gameId: String) {
        Log.d("NandiDebug", "saving game to firestore ${gameId}")
        authService.currentUserId?.let {
            currentCollection(it).document(gameId).set(FireStoreGame(gameId)).await()
        }
    }

    override suspend fun getGame(gameID: String): String? =
        authService.currentUserId?.let {
            try {
                val documentSnapshot = currentCollection(it).document(gameID).get().await()
                documentSnapshot?.let {
                    if (it.exists())
                        it.getString("gameID")
                    else null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    override suspend fun deleteGame(gameID: String) {
        authService.currentUserId?.let {
            currentCollection(it).document(gameID).delete().await()
        }
    }

    override suspend fun getAllGames(): List<String> {
        val querySnapshot = authService.currentUserId?.let { currentUser ->
            currentCollection(currentUser).get().await()
        }
        return querySnapshot?.documents?.map { it.id } ?: emptyList()
    }


    private fun currentCollection(userId: String) =
        db.collection(USER_COLLECTION).document(userId).collection(TODO_COLLECTION)

    companion object {
        private const val USER_COLLECTION = "users"
        private const val TODO_COLLECTION = "games"
    }

}
