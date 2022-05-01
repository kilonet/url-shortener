package re.swiss.url.shortener.service

import re.swiss.url.shortener.model.Url

interface UrlStorage {
    fun put(keyword: String, url: Url): Boolean
    fun get(key: String): Url?
    fun remove(key: String)
    fun size(): Int
}