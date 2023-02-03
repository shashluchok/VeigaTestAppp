package ru.armensarkisyan.veigatestapp.common.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.armensarkisyan.veigatestapp.features.first_step.FirstStepViewModel
import ru.armensarkisyan.veigatestapp.features.second_step.SecondStepViewModel


val viewModelsModule = module {

    viewModel { SecondStepViewModel(get()) }
    viewModel { FirstStepViewModel() }

}