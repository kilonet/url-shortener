package re.swiss.url.shortener

import re.swiss.url.shortener.service.ConcurrentHashMapUrlStorage
import re.swiss.url.shortener.service.HashmapUrlShortener
import re.swiss.url.shortener.service.NanoIdRandomGenerator
import re.swiss.url.shortener.service.ThreadExpiringUrlShortener
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors


fun main(args: Array<String>) {
    println("Hello World!")

    val threadPool = Executors.newScheduledThreadPool(1)

    val urlShortener = ThreadExpiringUrlShortener(
        HashmapUrlShortener(
            NanoIdRandomGenerator(),
            ConcurrentHashMapUrlStorage(ConcurrentHashMap())
        ), threadPool
    )

    val url = urlShortener.createAlias("google.com", Instant.now().toEpochMilli() + 2000)
    Thread.sleep(1000)
    urlShortener.getAlias(url!!.url.key)?.let {
        println("alias for google.com: ${it.key} is not null yet")
    }

    Thread.sleep(2000)
    if (urlShortener.getAlias(url!!.url.key) == null) {
        println("alias for google.com has expired and removed")
    }

    threadPool.shutdown()
}