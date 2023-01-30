package ru.armensarkisyan.veigatestapp.common.domain

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import ru.armensarkisyan.veigatestapp.common.data.net.responses.RatingsResponse

interface VeigaApi {

    @GET("/testAndroidData")
    @Headers("Content-Type: application/json")
    fun getTestData(): Call<RatingsResponse>

}