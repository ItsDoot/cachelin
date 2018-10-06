package pw.dotdash.cachelin.actor

import kotlinx.coroutines.CompletableDeferred

sealed class CacheMsg<out K : Any, out V : Any> {

    data class Get<out K : Any, V : Any>(val key: K, val response: CompletableDeferred<in V?>) : CacheMsg<K, V>()

    data class Set<out K : Any, out V : Any>(val key: K, val value: V) : CacheMsg<K, V>()

    data class Invalidate<out K : Any, out V : Any>(val key: K) : CacheMsg<K, V>()

    object InvalidateAll : CacheMsg<Any, Any>()
}