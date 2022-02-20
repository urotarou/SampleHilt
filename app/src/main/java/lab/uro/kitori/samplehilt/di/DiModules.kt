package lab.uro.kitori.samplehilt.di

import android.app.Application
import android.content.Context
import androidx.viewbinding.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import lab.uro.kitori.samplehilt.data.GithubApi
import lab.uro.kitori.samplehilt.data.UserRepositoryImpl
import lab.uro.kitori.samplehilt.domain.UserUseCase
import lab.uro.kitori.samplehilt.presentation.UserViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule() {
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = when {
            BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }
        addInterceptor(loggingInterceptor)

        connectTimeout(30, TimeUnit.SECONDS)
        callTimeout(30, TimeUnit.SECONDS)
    }.build()

    @Provides
    fun provideGithubApi(
        client: OkHttpClient
    ): GithubApi {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.github.com")
            .addConverterFactory(
                @OptIn(ExperimentalSerializationApi::class)
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(GithubApi::class.java)
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
class ActivityRetainedModule {
    @Provides
    fun provideUserViewModel(
        @ApplicationContext context: Context,
        useCase: UserUseCase
    ): UserViewModel = UserViewModel(context as Application, useCase)
}

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {
    @Provides
    fun provideUserRepositoryImpl(
        api: GithubApi
    ): UserRepositoryImpl = UserRepositoryImpl(api)

    @Provides
    fun provideUserUseCase(
        repository: UserRepositoryImpl
    ): UserUseCase = UserUseCase(repository)
}

//@Module
//@InstallIn(ViewModelComponent::class)
//abstract class ViewModelModule2 {
//    @Binds
//    abstract fun bindUserRepository(
//        repository: UserRepositoryImpl
//    ): UserRepository
//}
