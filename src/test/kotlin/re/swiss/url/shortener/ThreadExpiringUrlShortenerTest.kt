package re.swiss.url.shortener

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import re.swiss.url.shortener.model.ExpiringUrl
import re.swiss.url.shortener.service.HashmapUrlShortener
import re.swiss.url.shortener.service.NanoIdRandomGenerator
import re.swiss.url.shortener.service.ThreadExpiringUrlShortener
import java.time.Instant
import java.util.concurrent.Executors

class ThreadExpiringUrlShortenerTest {

    @Test
    fun entryRemovedOnlyAfterExpire() {
        // create alias
        val urlShortener = ThreadExpiringUrlShortener(HashmapUrlShortener(NanoIdRandomGenerator()), Executors.newScheduledThreadPool(1))
        val createAlias: ExpiringUrl? = urlShortener.createAlias("google.com", Instant.now().toEpochMilli() + 300)


        // until expire exist
        Thread.sleep(100)
        Assertions.assertTrue(urlShortener.getAlias(createAlias!!.url.key) != null)


        // after expire removed
        Thread.sleep(300)
        Assertions.assertTrue(urlShortener.getAlias(createAlias!!.url.key) == null)
        Assertions.assertTrue(urlShortener.storage().size == 0)
    }

}