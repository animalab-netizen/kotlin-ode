package br.com.lab.kotlin.ode.business.interactor

import br.com.lab.kotlin.ode.base.BaseUseCaseTest
import br.com.lab.kotlin.ode.business.dto.Output
import br.com.lab.kotlin.ode.business.dto.ValueOutput
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class SequenceUseCaseTest : BaseUseCaseTest() {
    private val secondParam = "teste"
    private val secondOutput = ValueOutput("secondOutput")
    private lateinit var secondUseCase: UseCase<String, String>
    private lateinit var sequenceUseCase: UseCase<Nothing, List<Output<*>>>

    @BeforeEach
    fun setupTest() {
        secondUseCase = createUseCase(secondParam, secondOutput)
        val builder = SequenceUseCase
            .builder()
            .add(useCase, param)
            .add(secondUseCase, secondParam)
        sequenceUseCase = spy(builder.build())

        whenever(secondUseCase.guard(eq(secondParam))).thenReturn(true)
        whenever(useCase.guard(eq(param))).thenReturn(true)
        whenever(sequenceUseCase.guard()).thenReturn(true)
    }

    private fun invokeSequence() {
        sequenceUseCase.process()
    }

    @Test
    fun `given two use cases, when sequence is invoked, then get return in insert order`() {
        invokeSequence()

        verify(sequenceUseCase).onResult(argThat {
            val values = this.value!!
            val first = values[0] == output
            val second = values[1] == secondOutput
            first && second
        })
    }

    @Test
    fun `given two use cases and one of them raises exception, then return has one error value`() {
        val exception = RuntimeException()
        whenever(secondUseCase.guard(any())).thenThrow(exception)

        invokeSequence()

        verify(secondUseCase).onError(eq(exception))

        verify(sequenceUseCase).onResult(argThat {
            val values = this.value!!
            val first = values[0] == output
            val second = values[1].isError()
            first && second
        })
    }

    @Test
    fun `given same sequence instance when invoked twice then results do not accumulate`() {
        sequenceUseCase.process()
        sequenceUseCase.process()

        verify(sequenceUseCase, times(2)).onResult(argThat {
            val values = this.value!!
            assertEquals(2, values.size)
            values[0] == output && values[1] == secondOutput
        })
    }
}
