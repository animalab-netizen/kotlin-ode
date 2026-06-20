package br.com.lab.kotlin.ode.business.interactor

import br.com.lab.kotlin.ode.business.dto.EmptyOutput
import br.com.lab.kotlin.ode.business.dto.ErrorOutput
import br.com.lab.kotlin.ode.business.dto.Output
import br.com.lab.kotlin.ode.business.dto.ValueOutput
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ChainedUseCaseTest {
    @Test
    fun `when first use case succeeds then second use case is executed`() {
        val first: UseCase<String, String> = mock()
        val second: UseCase<String, Int> = mock()
        whenever(first.execute("input")).thenReturn(ValueOutput("mapped"))
        whenever(second.execute("mapped")).thenReturn(ValueOutput(1))

        val chained = ChainedUseCase(first, second)
        val result = chained.execute("input")

        verify(second).execute("mapped")
        assertTrue(result is ValueOutput<Int>)
    }

    @Test
    fun `when first use case fails then error is propagated`() {
        val first: UseCase<String, String> = mock()
        val second: UseCase<String, Int> = mock()
        whenever(first.execute("input")).thenReturn(ErrorOutput(RuntimeException("boom")))

        val chained = ChainedUseCase(first, second)
        val result = chained.execute("input")

        assertTrue(result is ErrorOutput<Int>)
    }

    @Test
    fun `when first use case is empty then chain stays empty`() {
        val first: UseCase<String, String> = mock()
        val second: UseCase<String, Int> = mock()
        whenever(first.execute("input")).thenReturn(EmptyOutput())

        val chained = ChainedUseCase(first, second)
        val result: Output<Int> = chained.execute("input")

        assertTrue(result is EmptyOutput<Int>)
    }
}
