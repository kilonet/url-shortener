package re.swiss.url.shortener.service

import re.swiss.url.shortener.model.Url
import java.util.concurrent.ConcurrentHashMap

class ConcurrentHashMapUrlStorage(
    private val hashMap: ConcurrentHashMap<String, Url>
): UrlStorage {

    override fun put(keyword: String, url: Url): Boolean {
        return hashMap.putIfAbsent(keyword, url) == null
    }

    override fun get(key: String): Url? {
        return hashMap[key]
    }

    override fun remove(key: String) {
        hashMap.remove(key)
    }

    override fun size(): Int {
        return hashMap.size
    }
}