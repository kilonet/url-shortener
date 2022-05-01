package re.swiss.url.shortener

import re.swiss.url.shortener.infrastructure.SystemClock
import re.swiss.url.shortener.service.HashmapUrlShortener
import re.swiss.url.shortener.service.NanoIdRandomGenerator
import re.swiss.url.shortener.service.ThreadExpiringUrlShortener
import java.time.Instant


fun main(args: Array<String>) {
    println("Hello World!")

   /* ThreadExpiringUrlShortener(HashmapUrlShortener(NanoIdRandomGenerator()), SystemClock(), 100).let {
        val url = it.createAlias("google.com", Instant.now().epochSecond + 5)
        Thread.sleep(1000)
        it.getAlias(url!!.url.key)?.let {
            println("alias for google.com is not null: ${it.key}")
        }

        Thread.sleep(5000)
        if (it.getAlias(url!!.url.key) == null) {
            println("alias for google.com has expired")
        }
    }*/
}