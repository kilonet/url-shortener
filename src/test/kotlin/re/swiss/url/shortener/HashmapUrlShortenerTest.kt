package re.swiss.url.shortener

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.ThrowingSupplier
import org.mockito.Mockito
import re.swiss.url.shortener.service.ConcurrentHashMapUrlStorage
import re.swiss.url.shortener.exception.GenerateKeyFailedException
import re.swiss.url.shortener.service.HashmapUrlShortener
import re.swiss.url.shortener.service.NanoIdRandomGenerator
import re.swiss.url.shortener.service.RandomIdGenerator
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

class HashmapUrlShortenerTest {

    @Test
    fun keywordGenerate() {
        val urlShortener = HashmapUrlShortener(NanoIdRandomGenerator(), ConcurrentHashMapUrlStorage(ConcurrentHashMap()))
        val url = "http://somews.com/abc/1/page.html"
        val keyword = "myLovelyPage"

        val createdAlias = urlShortener.createAlias(url, keyword)
        val alias = urlShortener.getAlias(createdAlias!!.key)

        Assertions.assertTrue(alias?.url == url)
        Assertions.assertTrue(alias?.key == keyword)
    }

    @Test
    fun overwriteExistingNotAllowed() {
        val urlShortener = HashmapUrlShortener(NanoIdRandomGenerator(), ConcurrentHashMapUrlStorage(ConcurrentHashMap()))
        val url = "http://somews.com/abc/1/page.html"
        val anotherUrl = "http://somews.com/def/2/page.html"
        val keyword = "myLovelyPage"

        urlShortener.createAlias(url, keyword)
        val anotherUrlAlias = urlShortener.createAlias(anotherUrl, keyword)

        Assertions.assertTrue(anotherUrlAlias == null)
        Assertions.assertTrue(urlShortener.getAlias(keyword)?.url == url)
    }

    @Test
    fun randomIdGenerate() {
        val urlShortener = HashmapUrlShortener(NanoIdRandomGenerator(), ConcurrentHashMapUrlStorage(ConcurrentHashMap()))
        val url = "http://somews.com/abc/1/page.html"

        val alias = urlShortener.createAlias(url)

        Assertions.assertTrue(urlShortener.getAlias(alias!!.key)?.url == url)
    }

    @Test
    fun collisionsResolved() {
        val randomIdGenerator = Mockito.mock(RandomIdGenerator::class.java)
        Mockito.`when`(randomIdGenerator.randomId()).thenReturn("a", "a", "b")
        val urlShortener = HashmapUrlShortener(randomIdGenerator, ConcurrentHashMapUrlStorage(ConcurrentHashMap()))

        urlShortener.createAlias("google.com")
        val alias = urlShortener.createAlias("strava.com")

        Assertions.assertTrue(alias?.key == "b");
    }

    @Test()
    fun collisionsTimeout() {
        val randomIdGenerator = Mockito.mock(RandomIdGenerator::class.java)
        Mockito.`when`(randomIdGenerator.randomId()).thenReturn("a")
        val urlShortener = HashmapUrlShortener(randomIdGenerator, ConcurrentHashMapUrlStorage(ConcurrentHashMap()))

        urlShortener.createAlias("google.com")

        Assertions.assertTimeoutPreemptively(Duration.ofMillis(100), ThrowingSupplier {
            try {
                urlShortener.createAlias("strava.com")
            } catch (e: GenerateKeyFailedException) {
                return@ThrowingSupplier e
            }
        })

        Assertions.assertThrows(GenerateKeyFailedException::class.java) {
            urlShortener.createAlias("strava.com")
        }
    }

}