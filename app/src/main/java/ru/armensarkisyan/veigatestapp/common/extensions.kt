package ru.armensarkisyan.veigatestapp.common

const val SECONDS_IN_HOUR = 3600
const val SECONDS_IN_MINUTE = 60
const val MINUTES_IN_HOUR = 60
const val HOURS_IN_DAY = 24

fun Int.secondsToHoursMinutesSeconds(): Triple<String, String, String> {
    val resultHours = ((this / SECONDS_IN_HOUR) % HOURS_IN_DAY).toString()
    val resultMinutes = ((this / SECONDS_IN_MINUTE) % MINUTES_IN_HOUR).toString()
    val resultSeconds = (this % SECONDS_IN_MINUTE).toString()

    val formattedHoursString = if (resultHours.length < 2) {
        "0$resultHours"
    } else {
        resultHours
    }

    val formattedMinutesString = if (resultMinutes.length < 2) {
        "0$resultMinutes"
    } else {
        resultMinutes
    }

    val formattedSecondsString = if (resultSeconds.length < 2) {
        "0$resultSeconds"
    } else {
        resultSeconds
    }

    return Triple(formattedHoursString, formattedMinutesString, formattedSecondsString)
}
