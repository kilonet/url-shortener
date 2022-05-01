package re.swiss.url.shortener.infrastructure

import re.swiss.url.shortener.infrastructure.Clock
import java.time.Instant

class SystemClock: Clock {
    override fun now(): Long {
        return Instant.now().epochSecond
    }
}