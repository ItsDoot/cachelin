package pw.dotdash.cachelin.simple

import pw.dotdash.cachelin.Cache

class CombinedCache<K : Any, V : Any>(
    private val first: Cache<K, V>,
    private val second: Cache<K, V>
) : Cache<K, V> {

    override suspend fun get(key: K): V? = first.get(key) ?: second.get(key)

    override suspend fun set(key: K, value: V) {
        first.set(key, value)
        second.set(key, value)
    }

    override suspend fun invalidate(key: K) {
        first.invalidate(key)
        second.invalidate(key)
    }

    override suspend fun invalidateAll() {
        first.invalidateAll()
        second.invalidateAll()
    }
}