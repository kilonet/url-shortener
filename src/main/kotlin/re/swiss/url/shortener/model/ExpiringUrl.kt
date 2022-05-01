package re.swiss.url.shortener.model

import re.swiss.url.shortener.model.Url

data class ExpiringUrl(
    val url: Url,
    val expireAt: Long
)