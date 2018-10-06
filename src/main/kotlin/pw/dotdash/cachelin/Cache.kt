package pw.dotdash.cachelin

import pw.dotdash.cachelin.simple.CombinedCache

interface Cache<K : Any, V : Any> {

    suspend fun get(key: K): V?

    suspend fun getAll(): Iterable<V>

    suspend fun set(key: K, value: V)

    suspend fun invalidate(key: K)

    suspend fun invalidateAll()

    operator fun plus(other: Cache<K, V>): Cache<K, V> = CombinedCache(this, other)
}