package com.example.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.LoginRepository
import com.example.login.model.UserKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _userKeyState: MutableStateFlow<UserKey?> = MutableStateFlow(null)
    val userKeyState: StateFlow<UserKey?> = _userKeyState

    fun checkValidKey(userKey: UserKey, callbackIsValid: () -> Unit) {
        viewModelScope.launch {
            loginRepository.checkKey(userKey) { isValid ->
                if (isValid) {

                } else {

                }
            }
        }
    }


}