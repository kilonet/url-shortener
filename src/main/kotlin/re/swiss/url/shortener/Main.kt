package re.swiss.url.shortener

import re.swiss.url.shortener.service.HashmapUrlShortener
import re.swiss.url.shortener.service.NanoIdRandomGenerator
import re.swiss.url.shortener.service.ThreadExpiringUrlShortener
import java.time.Instant
import java.util.concurrent.Executors


fun main(args: Array<String>) {
    println("Hello World!")

    val threadPool = Executors.newScheduledThreadPool(1)
    ThreadExpiringUrlShortener(HashmapUrlShortener(NanoIdRandomGenerator()), threadPool).let {
        val url = it.createAlias("google.com", Instant.now().toEpochMilli() + 2000)
        Thread.sleep(1000)
        it.getAlias(url!!.url.key)?.let {
            println("alias for google.com [${it.key}] is not null yet")
        }

        Thread.sleep(2000)
        if (it.getAlias(url!!.url.key) == null) {
            println("alias for google.com has expired and removed")
        }
    }

    threadPool.shutdown()
}