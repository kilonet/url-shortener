package re.swiss.url.shortener

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import re.swiss.url.shortener.infrastructure.Clock
import re.swiss.url.shortener.model.ExpiringUrl
import re.swiss.url.shortener.service.HashmapUrlShortener
import re.swiss.url.shortener.service.NanoIdRandomGenerator
import re.swiss.url.shortener.service.ThreadExpiringUrlShortener

class ThreadExpiringUrlShortenerTest {

    @Test
    fun test() {
        // create alias
        val clock = Mockito.mock(Clock::class.java)
        Mockito.`when`(clock.now()).thenReturn(1)
        val urlShortener = ThreadExpiringUrlShortener(HashmapUrlShortener(NanoIdRandomGenerator()), clock, 10)
        val createAlias: ExpiringUrl? = urlShortener.createAlias("google.com", 1000)


        // until expire exist
        Thread.sleep(100)
        Assertions.assertTrue(urlShortener.getAlias(createAlias!!.url.key) != null)


        // after expire removed
        Mockito.`when`(clock.now()).thenReturn(1001)
        Thread.sleep(100)
        Assertions.assertTrue(urlShortener.getAlias(createAlias!!.url.key) == null)
        Assertions.assertTrue(urlShortener.storage().size == 0)
    }

}