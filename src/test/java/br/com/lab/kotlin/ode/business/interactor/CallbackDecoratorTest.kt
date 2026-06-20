package br.com.lab.kotlin.ode.business.interactor

import br.com.lab.kotlin.ode.base.BaseUseCaseTest
import br.com.lab.kotlin.ode.business.dto.Output
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test

internal class CallbackDecoratorTest: BaseUseCaseTest() {

    @Test
    fun callback() {
        val callback = spy(Callback())
        val decorator = spy(CallbackDecorator(useCase, callback::set))
        whenever(decorator.guard(eq(param))).thenReturn(true)

        decorator.process(param)

        verify(decorator, times(1)).onResult(any())
        //verify(callback, times(1)).set(any())
        //assertEquals(callback.data, output)
    }

    class Callback {
        lateinit var data: Output<String>
        fun set(data: Output<String>) {
            this.data = data
        }
    }
}