package ru.armensarkisyan.veigatestapp.features.second_step

import android.os.Build.VERSION_CODES.P
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.armensarkisyan.veigatestapp.common.domain.interactors.retrofit.RatingsInteractor
import ru.armensarkisyan.veigatestapp.common.ui.base.BaseViewModel
import kotlin.random.Random

class SecondStepViewModel(private val ratingsInteractor: RatingsInteractor) :
    BaseViewModel<SecondStepStates>() {

    private var downloadingJob: Job? = null
    private var timerJob: Job? = null
    private var loadingState: LoadingStates = LoadingStates.PAUSED
    private var ratingsJob: Job? = null
    enum class LoadingStates {
        RESUMED, PAUSED
    }

    private companion object {
        const val DELAY_TIME = 1000L
        const val MOCKED_LOADING_MAX_DURATION_SECONDS = 25
        const val MOCKED_LOADING_MIN_DURATION_SECONDS = 5
        const val MOCKED_LOADING_TICK_VALUE = 1
        const val MIN_RANDOM_DELAY = 0
        const val MAX_RANDOM_DELAY = 500
        const val MAX_LOADING_PERCENTAGE = 100
        const val MIN_LOADING_PERCENTAGE = 0
        const val TIMER_DURATION_SECONDS = 3600
        const val TIMER_MIN_SECONDS = 0

        const val MOCKED_RATINGS_LOADING_MAX_DURATION = 7
    }


    fun startTimer() {

        var seconds = TIMER_DURATION_SECONDS
        timerJob = viewModelScope.launch(Dispatchers.Main) {
            delay(DELAY_TIME)
            repeat(TIMER_DURATION_SECONDS) {
                if (seconds == TIMER_MIN_SECONDS) {
                    stopTimer()
                } else {
                    seconds--
                    withContext(Dispatchers.Main) {
                        _state.value =
                            SecondStepStates.TimerTickState(remainingSecondsAmount = seconds)
                    }
                }
                delay(DELAY_TIME)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob?.cancelChildren()
    }

    fun startMockedDownloading() {
        downloadingJob?.cancelChildren()
        downloadingJob?.cancel()
        downloadingJob = viewModelScope.launch(Dispatchers.IO) {
            delay(DELAY_TIME)
            launch {
                startRandomDownloading(loaderType = SecondStepLoaderTypes.FIRST)
            }
            delay(Random.nextInt(MIN_RANDOM_DELAY, MAX_RANDOM_DELAY).toLong())
            launch {
                startRandomDownloading(loaderType = SecondStepLoaderTypes.SECOND)
            }
        }
    }

    private suspend fun startRandomDownloading(
        loaderType: SecondStepLoaderTypes
    ) {
        withContext(Dispatchers.IO) {
            val seconds = Random.nextInt(
                MOCKED_LOADING_MIN_DURATION_SECONDS,
                MOCKED_LOADING_MAX_DURATION_SECONDS
            )
            var currentSecond = 0
            while (isActive)
                if (loadingState == LoadingStates.RESUMED) {
                    if (currentSecond >= seconds) {
                        cancel()
                    } else {
                        currentSecond += MOCKED_LOADING_TICK_VALUE
                    }
                    val rawPercentage =
                        (MAX_LOADING_PERCENTAGE.toFloat() / seconds * currentSecond).toInt()
                    val validPercentage =
                        if (rawPercentage in MIN_LOADING_PERCENTAGE..MAX_LOADING_PERCENTAGE) {
                            rawPercentage
                        } else {
                            MAX_LOADING_PERCENTAGE
                        }
                    withContext(Dispatchers.Main) {
                        _state.value = SecondStepStates.LoadingProgressState(
                            percentage = validPercentage,
                            loaderType = loaderType
                        )
                    }
                    delay(DELAY_TIME)
                }
        }
    }

    fun getRatings() {
        ratingsJob?.cancelChildren()
        ratingsJob?.cancel()

        ratingsJob = viewModelScope.launch(Dispatchers.IO) {
            delay(DELAY_TIME)

            launch {
                val seconds = MOCKED_RATINGS_LOADING_MAX_DURATION
                var currentSecond = 0
                while (isActive) {
                    if (currentSecond >= seconds - MOCKED_LOADING_TICK_VALUE) {
                        cancel()
                    } else {
                        currentSecond += MOCKED_LOADING_TICK_VALUE
                    }
                    val percentage =
                        (MAX_LOADING_PERCENTAGE.toFloat() / seconds * currentSecond).toInt()
                    withContext(Dispatchers.Main) {
                        val validPercentage =
                            if (percentage in MIN_LOADING_PERCENTAGE..MAX_LOADING_PERCENTAGE) {
                                percentage
                            } else {
                                MAX_LOADING_PERCENTAGE
                            }
                        _state.value = SecondStepStates.LoadingProgressState(
                            percentage = validPercentage,
                            loaderType = SecondStepLoaderTypes.RATINGS
                        )
                    }
                    delay(DELAY_TIME)
                }
            }
            delay(DELAY_TIME)
            launch {
                try {
                    val ratings = ratingsInteractor.getRatings()
                    if (ratings != null) {
                        withContext(Dispatchers.Main) {
                            _state.value = SecondStepStates.LoadingProgressState(
                                percentage = MAX_LOADING_PERCENTAGE,
                                loaderType = SecondStepLoaderTypes.RATINGS
                            )
                            _state.value = SecondStepStates.DataLoadedState(ratings)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            _state.value = SecondStepStates.ErrorState
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        _state.value = SecondStepStates.ErrorState
                    }
                } finally {
                    ratingsJob?.cancel()
                    ratingsJob?.cancelChildren()
                }
            }

        }

    }

    fun setLoadingState(loadingState: LoadingStates) {
        this.loadingState = loadingState
    }

}