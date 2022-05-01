package re.swiss.url.shortener.service

import re.swiss.url.shortener.model.ExpiringUrl

interface ExpiringUrlShortener : UrlShortener {

    fun createAlias(url: String, expire: Long, keyword: String? = null): ExpiringUrl?

}