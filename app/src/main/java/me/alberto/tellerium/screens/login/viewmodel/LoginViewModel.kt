package me.alberto.tellerium.screens.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.alberto.tellerium.data.domain.usecase.SetLoginUseCase
import me.alberto.tellerium.screens.common.base.BaseViewModel
import me.alberto.tellerium.util.livedata.Event
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val setLoginUseCase: SetLoginUseCase) :
    BaseViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String> = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String> = _passwordError

    private val _navigate = MutableLiveData<Event<Unit>>()
    val navigate: LiveData<Event<Unit>> = _navigate


    fun validateCredentials() {
        when {
            email.value.isNullOrEmpty() && password.value.isNullOrEmpty() -> {
                _emailError.postValue("Email cannot be empty")
                _passwordError.postValue("Password cannot be empty")
                return
            }

            email.value.isNullOrEmpty() -> _emailError.postValue("Email cannot be empty")

            password.value.isNullOrEmpty() -> {
                _passwordError.postValue("Password cannot be empty")
                return
            }


            email.value?.trim() != "test@tellerium.io" || password.value?.trim() != "password" -> {
                _passwordError.postValue("Password or email invalid")
                return
            }

            else -> {
                viewModelScope.launch {
                    try {
                        setLoginUseCase.execute(true)
                        _navigate.postValue(Event(Unit))
                    } catch (error: Exception) {
                        Timber.d("Some error happened: ${error.message}")
                    }
                }
            }


        }
    }
}