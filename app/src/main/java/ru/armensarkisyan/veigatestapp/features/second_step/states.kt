package ru.armensarkisyan.veigatestapp.features.second_step

import ru.armensarkisyan.veigatestapp.common.data.net.responses.Raiting
import ru.armensarkisyan.veigatestapp.features.first_step.FirstStepStates

sealed class SecondStepStates {
    data class DataLoadedState(val ratings: Map<String, Raiting>) : SecondStepStates()
    data class LoadingProgressState(val percentage: Int, val loaderType: SecondStepLoaderTypes) : SecondStepStates()
    data class TimerTickState(val remainingSecondsAmount: Int):SecondStepStates()

    object ErrorState : SecondStepStates()
}

enum class SecondStepLoaderTypes {
    FIRST, SECOND, RATINGS
}