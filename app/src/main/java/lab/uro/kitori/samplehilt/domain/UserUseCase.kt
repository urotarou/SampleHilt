package lab.uro.kitori.samplehilt.domain

import lab.uro.kitori.samplehilt.data.UserRepository
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend fun getUser(): User = repository.getUser("urotarou").let {
        User(it.name, it.url)
    }
}
