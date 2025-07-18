package com.androbeat.androbeatagent.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androbeat.androbeatagent.data.repository.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val appDatabase = AppDatabase.getDatabase(application)
    private val _clientId = MutableLiveData<String>()
    val clientId: LiveData<String> get() = _clientId

    fun getClientIdFromRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedClientId = appDatabase.clientIdDao().getClientId()
            withContext(Dispatchers.Main) {
                savedClientId?.let {
                    _clientId.value = it.name
                }
            }
        }
    }
}