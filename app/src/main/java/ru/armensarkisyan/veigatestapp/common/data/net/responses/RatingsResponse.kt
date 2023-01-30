package ru.armensarkisyan.veigatestapp.common.data.net.responses

data class RatingsResponse(
    val raitings: Map<String, Raiting>
)

data class Raiting(
    val image: String,
    val title: String
)


