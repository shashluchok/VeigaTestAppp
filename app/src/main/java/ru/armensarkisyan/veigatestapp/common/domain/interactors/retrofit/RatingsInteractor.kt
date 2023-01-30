package ru.armensarkisyan.veigatestapp.common.domain.interactors.retrofit

import ru.armensarkisyan.veigatestapp.common.data.net.responses.Raiting
import ru.armensarkisyan.veigatestapp.common.domain.VeigaApi

class RatingsInteractor(
    private val api: VeigaApi,
) {
    suspend fun getRatings(): Map<String, Raiting>? {
        val request = api.getTestData().execute()
        return if (request.isSuccessful) {
            request.body()?.raitings?.let {
                return it
            }
        } else null
    }
}