package com.example.project.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.UserKey
import com.example.domain.repository.FireStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val fireStoreRepository: FireStoreRepository
) : ViewModel() {

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun checkValidKey(userKeyString: String) {
        val userKey = UserKey(userKey = userKeyString)
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val result = fireStoreRepository.checkUserKey(userKey)
            if (result.isSuccess) {
                _loginState.value = if (result.getOrDefault(false)) LoginState.Success else LoginState.KeyNotFound
            } else {
                _loginState.value = LoginState.KeyNotFound
            }
        }
    }

    fun resetToIdle() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    data object Idle: LoginState()
    data object Loading: LoginState()
    data object Success: LoginState()
    data object KeyNotFound : LoginState()
}