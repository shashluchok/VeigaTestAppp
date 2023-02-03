package ru.armensarkisyan.veigatestapp.features.first_step

import ru.armensarkisyan.veigatestapp.common.data.net.responses.Raiting
import ru.armensarkisyan.veigatestapp.features.second_step.SecondStepStates

sealed class FirstStepStates {
    data class LoadingProgressState(val percentage: Int) : FirstStepStates()
}