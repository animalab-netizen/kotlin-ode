package br.com.lab.kotlin.ode.business.interactor

/**
 *
 * An UseCaseDispatcher can run UseCase on coroutines
 * Processable's methods are executed on the `executeOn` dispatcher
 * UseCase's outputting methods are executed on the `resultOn` dispatcher
 */

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UseCaseDispatcher<P, R>(
    private val useCase: UseCase<P, R>,
    private val executeOn: CoroutineDispatcher = Dispatchers.IO,
    private val resultOn: CoroutineDispatcher = Dispatchers.Main
) {
    fun dispatch(param: P? = null): Job {
        return CoroutineScope(executeOn).launch {
            try {
                if (useCase.guard(param)) {
                    val output = useCase.execute(param)
                    withContext(resultOn) {
                        useCase.onResult(output)
                    }
                } else {
                    useCase.onGuardError()
                }
            } catch (error: Exception) {
                withContext(resultOn) {
                    useCase.onError(error)
                }
            }
        }
    }
}
