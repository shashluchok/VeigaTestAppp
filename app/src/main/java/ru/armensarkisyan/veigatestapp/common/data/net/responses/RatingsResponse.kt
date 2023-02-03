package ru.armensarkisyan.veigatestapp.common.data.net.responses

import com.google.gson.annotations.SerializedName

data class RatingsResponse(
    @SerializedName("raitings")
    val ratings: Map<String, Raiting>
)

data class Raiting(

    @SerializedName("image")
    val image: String,

    @SerializedName("title")
    val title: String
)
