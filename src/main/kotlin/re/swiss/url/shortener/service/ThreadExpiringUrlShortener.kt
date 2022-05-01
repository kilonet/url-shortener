package re.swiss.url.shortener.service

import re.swiss.url.shortener.model.ExpiringUrl
import java.time.Instant
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class ThreadExpiringUrlShortener(
    private val inner: UrlShortener,
    private val threadPool: ScheduledExecutorService
): ExpiringUrlShortener, UrlShortener by inner {

    override fun createAlias(url: String, expire: Long, keyword: String?): ExpiringUrl? {
        return inner.createAlias(url, keyword)?.let {
            ExpiringUrl(it, expire).also { expiringUrl ->
                threadPool.schedule({
                    inner.storage().remove(it.key)
                }, expiringUrl.expireAt - Instant.now().toEpochMilli(), TimeUnit.MILLISECONDS)
            }
        }
    }
}
