package lab.uro.kitori.samplehilt.data

import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: GithubApi
) : UserRepository {
    override suspend fun getUser(name: String): UserResponse = api.getUser(name)
}
