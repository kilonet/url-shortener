package re.swiss.url.shortener.service

import re.swiss.url.shortener.exception.GenerateKeyFailedException
import re.swiss.url.shortener.model.Url

class HashmapUrlShortener(private val randomIdGenerator: RandomIdGenerator,
                          private val storage: UrlStorage) : UrlShortener {

    private val maxAttempts = 5

    override fun createAlias(url: String, keyword: String?): Url? {
        if (keyword != null) {
            val newUrl = Url(keyword, url)
            return if (storage.put(keyword, newUrl)) newUrl else null
        } else {
            for (i in 0..maxAttempts) {
                val key = randomIdGenerator.randomId()
                val newUrl = Url(key, url)
                if (storage.put(key, newUrl)) {
                    return newUrl
                }
            }
        }

        throw GenerateKeyFailedException()
    }

    override fun getAlias(key: String): Url? {
        return storage.get(key)
    }

    override fun storage(): UrlStorage {
        return storage
    }
}