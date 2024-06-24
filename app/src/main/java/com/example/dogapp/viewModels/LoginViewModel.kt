package com.example.dogapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogapp.room.User
import com.example.dogapp.room.UserDao
import com.example.dogapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    private val _login = MutableSharedFlow<Resource<User>>()
    val login = _login.asSharedFlow()

    // Función para el login
    fun login(email: String, password: String) {
        viewModelScope.launch { _login.emit(Resource.Loading()) }

        // Logica para e incio de sesion
        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getUserByEmailAndPassword(email, password)
            if (user != null) {
                viewModelScope.launch {
                    _login.emit(Resource.Success(user))
                }
            } else {
                viewModelScope.launch {
                    _login.emit(Resource.Error("Credenciales incorrectas"))
                }
            }
        }
    }
}