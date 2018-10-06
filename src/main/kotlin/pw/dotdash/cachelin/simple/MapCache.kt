package pw.dotdash.cachelin.simple

import pw.dotdash.cachelin.Cache
import java.util.concurrent.ConcurrentHashMap

class MapCache<K : Any, V : Any> : Cache<K, V> {

    private val map = ConcurrentHashMap<K, V>()

    override suspend fun get(key: K): V? = map[key]

    override suspend fun toMap(): Map<K, V> = map.toMap()

    override suspend fun set(key: K, value: V) {
        map[key] = value
    }

    override suspend fun invalidate(key: K) {
        map.remove(key)
    }

    override suspend fun invalidateAll() {
        map.clear()
    }
}