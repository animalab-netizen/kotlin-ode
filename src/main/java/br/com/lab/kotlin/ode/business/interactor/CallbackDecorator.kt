package br.com.lab.kotlin.ode.business.interactor

import br.com.lab.kotlin.ode.business.dto.ErrorOutput
import br.com.lab.kotlin.ode.business.dto.Output

internal class CallbackDecorator<P, R>(
    useCase: UseCase<P, R>,
    private val callback: (Output<R>) -> Unit
) : UseCaseDecorator<P, R>(useCase) {
    override fun onResult(output: Output<R>) {
        super.onResult(output)
        callback.invoke(output)
    }

    override fun onError(error: Throwable) {
        super.onError(error)
        callback.invoke(ErrorOutput(error))
    }
}
