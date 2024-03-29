package lab.uro.kitori.samplehilt.data

import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {
    @GET("/users/{name}")
    suspend fun getUser(
        @Path("name") name: String
    ): UserResponse
}
