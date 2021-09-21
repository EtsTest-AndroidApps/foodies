package com.moose.foodies.features.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class AuthViewmodel: ViewModel() {

    private val _screen = MutableLiveData(0)
    val screen: LiveData<Int> = _screen
    fun changeScreen(value: Int) = value.also { _screen.value = it }


    fun login(email: String, password: String) {
        TODO("Not yet implemented")
    }

    fun forgot(email: String) {
        TODO("Not yet implemented")
    }

    fun signup(email: String, password: String) {
        TODO("Not yet implemented")
    }


}