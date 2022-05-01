package re.swiss.url.shortener.service

import re.swiss.url.shortener.infrastructure.Clock
import re.swiss.url.shortener.model.ExpiringUrl
import re.swiss.url.shortener.model.Url
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ThreadExpiringUrlShortener(
    private val inner: UrlShortener,
    private val clock: Clock,
    private val cleanDelayMs: Long = 3600 * 1000
): ExpiringUrlShortener, UrlShortener by inner {

    private val expireView: SortedSet<ExpiringUrl> = TreeSet { a, b -> a.expireAt.compareTo(b.expireAt) }

    init {
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay({
           clearExpired()
        }, 0, cleanDelayMs, TimeUnit.MILLISECONDS)
    }

    override fun createAlias(url: String, expire: Long, keyword: String?): ExpiringUrl? {
        return inner.createAlias(url, keyword)?.let {
            ExpiringUrl(it, expire).also { expiringUrl ->
                expireView.add(expiringUrl)
            }
        }
    }

    private fun clearExpired() {
        expireView.headSet(ExpiringUrl(emptyUrl, clock.now())).toList().forEach {
            inner.storage().remove(it.url.key)
            expireView.remove(it)
        }
    }
}

private val emptyUrl = Url("", "")