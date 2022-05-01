package re.swiss.url.shortener.service

import re.swiss.url.shortener.model.Url
import java.util.concurrent.ConcurrentHashMap

class HashmapUrlShortener(private val randomIdGenerator: RandomIdGenerator) : UrlShortener {

    private val storage = ConcurrentHashMap<String, Url>()
    private val maxAttempts = 5

    override fun createAlias(url: String, keyword: String?): Url? {
        if (keyword != null) {
            val newUrl = Url(keyword, url)
            return if (storage.putIfAbsent(keyword, newUrl) == null) {
                newUrl
            } else {
                null
            }
        } else {
            for (i in 0..maxAttempts) {
                val key = randomIdGenerator.randomId()
                val newUrl = Url(key, url)
                storage.putIfAbsent(key, newUrl) ?: return newUrl
            }
        }

        throw IllegalStateException();
    }

    override fun getAlias(key: String): Url? {
        return storage[key]
    }

    override fun storage(): ConcurrentHashMap<String, Url> {
        return storage;
    }
}