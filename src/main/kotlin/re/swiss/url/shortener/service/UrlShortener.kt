package re.swiss.url.shortener.service

import re.swiss.url.shortener.model.Url
import java.util.concurrent.ConcurrentHashMap

interface UrlShortener {

    fun createAlias(url: String, keyword: String? = null): Url?

    fun getAlias(key: String): Url?

    fun storage(): ConcurrentHashMap<String, Url>

}