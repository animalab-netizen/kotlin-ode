package br.com.lab.kotlin.ode.business.interactor

import br.com.lab.kotlin.ode.business.dto.Output
import br.com.lab.kotlin.ode.business.dto.ValueOutput

class SequenceUseCase private constructor(private val units: List<UseCaseUnit<*, *>>) :
    UseCase<Nothing, List<Output<*>>>() {

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    override fun execute(param: Nothing?): Output<List<Output<*>>> {
        val stream = mutableListOf<Output<*>>()

        for (unit in units) {
            val output = unit.process()
            stream.add(output)
        }

        return ValueOutput(stream.toList())
    }

    class Builder {
        private val list = mutableListOf<UseCaseUnit<*, *>>()

        fun <P, R> add(useCase: UseCase<P, R>, param: P? = null): Builder {
            list.add(UseCaseUnit(useCase, param))
            return this
        }

        fun build(): UseCase<Nothing, List<Output<*>>> {
            return SequenceUseCase(list.toList())
        }
    }
}
