package re.swiss.url.shortener.infrastructure

interface Clock {
    fun now(): Long
}