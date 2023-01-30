package ru.armensarkisyan.veigatestapp.features.second_step

import ru.armensarkisyan.veigatestapp.common.data.net.responses.Raiting

sealed class SecondStepStates {
    data class DataLoadedState(val ratings: Map<String, Raiting>) : SecondStepStates()
    object ErrorState : SecondStepStates()
}