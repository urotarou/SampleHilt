package lab.uro.kitori.samplehilt.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)
