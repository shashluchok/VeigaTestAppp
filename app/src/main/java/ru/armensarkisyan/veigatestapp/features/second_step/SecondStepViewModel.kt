package ru.armensarkisyan.veigatestapp.features.second_step

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.armensarkisyan.veigatestapp.common.domain.interactors.retrofit.RatingsInteractor
import ru.armensarkisyan.veigatestapp.common.ui.base.BaseViewModel

class SecondStepViewModel(private val ratingsInteractor: RatingsInteractor) :
    BaseViewModel<SecondStepStates>() {

    fun getRatings() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ratings = ratingsInteractor.getRatings()
                if(ratings!=null){
                    withContext(Dispatchers.Main) {
                        _state.value = SecondStepStates.DataLoadedState(ratings)
                    }
                }
                else {
                    withContext(Dispatchers.Main) {
                        _state.value = SecondStepStates.ErrorState
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _state.value = SecondStepStates.ErrorState
                }
            }
        }
    }


}