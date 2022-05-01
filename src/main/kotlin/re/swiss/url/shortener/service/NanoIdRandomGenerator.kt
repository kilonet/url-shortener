package re.swiss.url.shortener.service

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import java.security.SecureRandom

class NanoIdRandomGenerator: RandomIdGenerator {

    private val alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
    private val random = SecureRandom();

    override fun randomId(): String {
        return NanoIdUtils.randomNanoId(random, alphabet, 6)
    }
}