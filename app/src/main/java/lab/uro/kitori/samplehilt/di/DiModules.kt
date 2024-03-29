package lab.uro.kitori.samplehilt.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import lab.uro.kitori.samplehilt.data.GitHubApi
import lab.uro.kitori.samplehilt.data.UserRepository
import lab.uro.kitori.samplehilt.data.UserRepositoryImpl
import lab.uro.kitori.samplehilt.domain.UserUseCase
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(loggingInterceptor)

        connectTimeout(30, TimeUnit.SECONDS)
        callTimeout(30, TimeUnit.SECONDS)
    }.build()

    @Provides
    fun provideGithubApi(
        client: OkHttpClient
    ): GitHubApi {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.github.com")
            .addConverterFactory(
                @OptIn(ExperimentalSerializationApi::class)
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(GitHubApi::class.java)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelUseCaseModule {
    @Provides
    fun provideUserUseCase(
        repository: UserRepository
    ): UserUseCase = UserUseCase(repository)
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelRepositoryModule {
    @Binds
    abstract fun bindUserRepository(
        repository: UserRepositoryImpl
    ): UserRepository
}
