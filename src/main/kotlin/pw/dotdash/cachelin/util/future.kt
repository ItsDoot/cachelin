package pw.dotdash.cachelin.util

import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

fun <T : Any> CoroutineScope.future(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend () -> T
): CompletableFuture<T> = FutureContinuation<T>(context).apply { block.startCoroutine(this) }

internal class FutureContinuation<T : Any>(override val context: CoroutineContext) :
    CompletableFuture<T>(), Continuation<T> {

    override fun resumeWith(result: Result<T>) {
        if (result.isSuccess) {
            complete(result.getOrNull())
        } else {
            completeExceptionally(result.exceptionOrNull())
        }
    }
}