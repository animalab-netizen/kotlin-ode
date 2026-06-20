package br.com.lab.kotlin.ode.business.interactor

import br.com.lab.kotlin.ode.business.dto.Output

internal abstract class UseCaseDecorator<P, R>(private val useCase: UseCase<P, R>) : UseCase<P, R>() {

    override fun onError(error: Throwable) {
        useCase.onError(error)
    }

    override fun execute(param: P?): Output<R> {
        return useCase.execute(param)
    }

    override fun onResult(output: Output<R>) {
        useCase.onResult(output)
    }

    override fun guard(param: P?): Boolean {
        return useCase.guard(param)
    }
}
