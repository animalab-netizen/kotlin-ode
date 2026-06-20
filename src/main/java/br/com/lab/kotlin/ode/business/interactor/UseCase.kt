package br.com.lab.kotlin.ode.business.interactor

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PROTECTED
import br.com.lab.kotlin.ode.business.dto.ErrorOutput
import br.com.lab.kotlin.ode.business.dto.Output

abstract class UseCase<P, R> {
    open fun process(param: P? = null) {
        try {
            if (guard(param)) {
                execute(param).also {
                    onResult(it)
                }
            } else {
                onGuardError()
            }
        } catch (error: Exception) {
            onError(error)
        }
    }

    @VisibleForTesting(otherwise = PROTECTED)
    abstract fun execute(param: P? = null): Output<R>

    @VisibleForTesting(otherwise = PROTECTED)
    open fun onError(error: Throwable) = onResult(ErrorOutput(error))

    @VisibleForTesting(otherwise = PROTECTED)
    open fun onResult(output: Output<R>) {
    }

    @VisibleForTesting(otherwise = PROTECTED)
    open fun guard(param: P? = null): Boolean {
        return true
    }

    @VisibleForTesting(otherwise = PROTECTED)
    open fun onGuardError() {
    }
}
