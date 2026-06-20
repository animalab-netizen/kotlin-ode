package br.com.lab.kotlin.ode.business.interactor

import br.com.lab.kotlin.ode.business.dto.EmptyOutput
import br.com.lab.kotlin.ode.business.dto.ErrorOutput
import br.com.lab.kotlin.ode.business.dto.Output
import br.com.lab.kotlin.ode.business.dto.ValueOutput

class ChainedUseCase<P, R, T>(
    private val first: UseCase<P, R>,
    private val second: UseCase<R, T>
) : UseCase<P, T>() {
    override fun execute(param: P?): Output<T> {
        return when (val intermediate = first.execute(param)) {
            is ValueOutput<R> -> second.execute(intermediate.value)
            is ErrorOutput<R> -> ErrorOutput(intermediate.error, null)
            is EmptyOutput<R> -> EmptyOutput()
        }
    }
}
