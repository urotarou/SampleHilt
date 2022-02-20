package lab.uro.kitori.samplehilt.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import lab.uro.kitori.samplehilt.domain.User
import lab.uro.kitori.samplehilt.domain.UserUseCase
import javax.inject.Inject

class UserViewModel @Inject constructor(
    @ApplicationContext app: Application,
    private val useCase: UserUseCase
) : AndroidViewModel(app) {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getUser() = viewModelScope.launch {
        runCatching {
            useCase.getUser()
        }.onSuccess {
            _user.value = it
        }.onFailure {
            _error.value = it.message ?: "error"
        }
    }
}
