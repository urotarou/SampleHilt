package lab.uro.kitori.samplehilt.data

interface UserRepository {
    suspend fun getUser(name: String): UserResponse
}
