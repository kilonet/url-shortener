package re.swiss.url.shortener.model

data class ExpiringUrl(
    val url: Url,
    val expireAt: Long
)