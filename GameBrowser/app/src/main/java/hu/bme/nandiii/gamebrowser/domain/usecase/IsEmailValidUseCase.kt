package hu.bme.nandiii.gamebrowser.domain.usecase

class IsEmailValidUseCase {

    operator fun invoke(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

}