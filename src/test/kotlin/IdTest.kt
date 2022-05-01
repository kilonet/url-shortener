import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import org.junit.jupiter.api.Test
import java.security.SecureRandom
import kotlin.math.max

class IdTest {
//    var DEFAULT_ALPHABET = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
    var DEFAULT_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray()

    @Test
    fun test() {
        val set = HashSet<String>()
        var totalAttempts = 0;
        var maxAttempts = 0;
        val secureRandom = SecureRandom()
        for (i in 0..100_000_000) {
            var add = false;
            var attempts = 0;
            while (!add) {
                totalAttempts++
                attempts++
                add = set.add(NanoIdUtils.randomNanoId(secureRandom, DEFAULT_ALPHABET, 6))
            }
            maxAttempts = max(maxAttempts, attempts)
        }

        println(totalAttempts)
        println(maxAttempts)

    }

}