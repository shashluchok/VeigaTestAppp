package ru.armensarkisyan.veigatestapp.common.ui.base

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.java.KoinJavaComponent.inject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException

open class BaseViewModel<STATE> : ViewModel() {

    protected val _state: MutableLiveData<STATE> = MutableLiveData()
    val state: LiveData<STATE> = _state

    fun clearState() {
        _state.value = null
    }

}