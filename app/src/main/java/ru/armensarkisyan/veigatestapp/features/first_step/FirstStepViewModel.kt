package ru.armensarkisyan.veigatestapp.features.first_step

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.armensarkisyan.veigatestapp.common.ui.base.BaseViewModel

class FirstStepViewModel() :
    BaseViewModel<FirstStepStates>() {

    private var mockedJob: Job? = null
    private var loadingState:LoadingStates = LoadingStates.PAUSED

    enum class LoadingStates {
        RESUMED, PAUSED
    }

    private companion object {
        const val MOCKED_LOADING_DELAY_TIME = 1000L
        const val MOCKED_LOADING_DURATION_SECONDS = 14
        const val MOCKED_LOADING_TICK_VALUE = 1
        const val MAX_LOADING_PERCENTAGE = 100
        const val MIN_LOADING_PERCENTAGE = 0
    }

    fun startMockedLoading() {
        mockedJob?.cancel()
        mockedJob = viewModelScope.launch(Dispatchers.IO) {
            delay(MOCKED_LOADING_DELAY_TIME)
            var currentSecond = 0
            while (isActive)

                if (loadingState == LoadingStates.RESUMED) {
                    if (currentSecond >= MOCKED_LOADING_DURATION_SECONDS) {
                        cancel()
                    }
                    else {
                        currentSecond += MOCKED_LOADING_TICK_VALUE
                    }
                    val rawPercentage = (MAX_LOADING_PERCENTAGE.toFloat() / MOCKED_LOADING_DURATION_SECONDS * currentSecond).toInt()
                    val validPercentage = if (rawPercentage in MIN_LOADING_PERCENTAGE..MAX_LOADING_PERCENTAGE)
                    {
                        rawPercentage
                    } else {
                        MAX_LOADING_PERCENTAGE
                    }
                    withContext(Dispatchers.Main) {
                       _state.value = FirstStepStates.LoadingProgressState(percentage = validPercentage)
                    }
                    delay(MOCKED_LOADING_DELAY_TIME)
                }
        }
    }

    fun setLoadingState(loadingState: LoadingStates){
        this.loadingState = loadingState
    }

}