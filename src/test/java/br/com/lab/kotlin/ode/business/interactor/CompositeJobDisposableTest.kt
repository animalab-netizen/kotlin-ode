package br.com.lab.kotlin.ode.business.interactor

import kotlinx.coroutines.Job
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

internal class CompositeJobDisposableTest {
    @Test
    fun `when completed jobs are tracked then purge removes them`() {
        val disposable = CompositeJobDisposable()
        val first = Job()
        val second = Job()

        disposable.add(first)
        disposable.add(second)
        first.complete()

        assertEquals(1, disposable.size())
    }

    @Test
    fun `when cancel is called then all active jobs are cancelled`() {
        val disposable = CompositeJobDisposable()
        val first = Job()
        val second = Job()

        disposable.add(first)
        disposable.add(second)

        disposable.cancel()

        assertFalse(first.isActive)
        assertFalse(second.isActive)
        assertEquals(0, disposable.size())
    }
}
