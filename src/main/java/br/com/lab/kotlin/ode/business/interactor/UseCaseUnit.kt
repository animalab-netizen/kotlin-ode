package br.com.lab.kotlin.ode.business.interactor

import br.com.lab.kotlin.ode.business.dto.EmptyOutput
import br.com.lab.kotlin.ode.business.dto.Output

internal data class UseCaseUnit<P, R>(val useCase: UseCase<P, R>, val param: P?) {
    fun process(): Output<R> {
        val callback = Callback<R>()
        val decorator = CallbackDecorator(useCase, callback::set)
        decorator.process(param)
        return callback.output
    }

    private class Callback<R> {
        var output: Output<R> = EmptyOutput()

        fun set(value: Output<R>) {
            output = value
        }
    }
}
