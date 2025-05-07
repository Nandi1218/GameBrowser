package hu.bme.nandiii.gamebrowser.data.auth


import hu.bme.nandiii.gamebrowser.domain.User
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val currentUserId: String?

    val hasUser: Boolean

    val currentUser: Flow<User?>

    suspend fun signUp(
        email: String, password: String,
    )

    suspend fun authenticate(
        email: String,
        password: String,
    )

    suspend fun sendRecoveryEmail(email: String)

    suspend fun deleteAccount()

    suspend fun signOut()
}