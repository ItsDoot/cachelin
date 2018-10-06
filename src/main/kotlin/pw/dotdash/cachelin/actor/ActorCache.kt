package pw.dotdash.cachelin.actor

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.actor
import pw.dotdash.cachelin.Cache

fun <K : Any, V : Any> CoroutineScope.cache() = actor<CacheMsg<K, V>> {
    val cache = mutableMapOf<K, V>()

    for (msg in channel) {
        when (msg) {
            is CacheMsg.Get -> msg.response.complete(cache[msg.key])
            is CacheMsg.GetAll -> msg.response.complete(cache.values)
            is CacheMsg.Set -> cache[msg.key] = msg.value
            is CacheMsg.Invalidate -> cache.remove(msg.key)
            CacheMsg.InvalidateAll -> cache.clear()
        }
    }
}

class ActorCache<K : Any, V : Any>(scope: CoroutineScope = GlobalScope) : Cache<K, V> {

    private val actor = scope.cache<K, V>()

    override suspend fun get(key: K): V? =
        CompletableDeferred<V?>().apply {
            actor.send(CacheMsg.Get(key, this))
        }.await()

    override suspend fun getAll(): Iterable<V> =
        CompletableDeferred<Iterable<V>>().apply {
            actor.send(CacheMsg.GetAll(this))
        }.await()

    override suspend fun set(key: K, value: V) {
        actor.send(CacheMsg.Set(key, value))
    }

    override suspend fun invalidate(key: K) {
        actor.send(CacheMsg.Invalidate(key))
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun invalidateAll() {
        actor.send(CacheMsg.InvalidateAll as CacheMsg<K, V>)
    }
}