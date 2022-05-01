package re.swiss.url.shortener.service

import re.swiss.url.shortener.model.ExpiringUrl
import java.lang.IllegalArgumentException
import java.time.Instant
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class ThreadExpiringUrlShortener(
    private val delegate: UrlShortener,
    private val executor: ScheduledExecutorService
): ExpiringUrlShortener, UrlShortener by delegate {

    override fun createAlias(url: String, expire: Long, keyword: String?): ExpiringUrl? {
        if (expire < Instant.now().toEpochMilli()) {
            throw IllegalArgumentException("expire should be in the future")
        }
        return delegate.createAlias(url, keyword)?.let {
            ExpiringUrl(it, expire).also { expiringUrl ->
                executor.schedule({
                    delegate.storage().remove(it.key)
                }, expiringUrl.expireAt - Instant.now().toEpochMilli(), TimeUnit.MILLISECONDS)
            }
        }
    }
}
