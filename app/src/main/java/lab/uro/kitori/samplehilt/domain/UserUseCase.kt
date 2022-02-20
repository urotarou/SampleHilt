package lab.uro.kitori.samplehilt.domain

import lab.uro.kitori.samplehilt.data.UserRepositoryImpl
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repository: UserRepositoryImpl
) {
    suspend fun getUser(): User = repository.getUser("urotarou").let {
        User(it.name, it.url)
    }
}
