package br.com.lab.kotlin.ode.gateway.mvvm

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import br.com.lab.kotlin.ode.business.dto.ValueOutput
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BaseViewModelBehaviorTest {
    private lateinit var viewModel: SampleViewModel

    @BeforeEach
    fun setup() {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
            override fun postToMainThread(runnable: Runnable) = runnable.run()
            override fun isMainThread(): Boolean = true
        })
        viewModel = SampleViewModel()
    }

    @AfterEach
    fun teardown() {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @Test
    fun `when two observers subscribe to same legacy channel then both receive values`() {
        val ownerOne = lifecycleOwner()
        val ownerTwo = lifecycleOwner()
        val observed = mutableListOf<String>()

        viewModel.observe("legacy", ownerOne, Observer { observed.add("one:${(it as ValueOutput<*>).value}") })
        viewModel.observe("legacy", ownerTwo, Observer { observed.add("two:${(it as ValueOutput<*>).value}") })

        viewModel.emitLegacy("ok")

        assertEquals(listOf("one:ok", "two:ok"), observed)
    }

    @Test
    fun `when typed channel is observed from viewmodel then typed value is delivered`() {
        val owner = lifecycleOwner()
        var observed: String? = null

        viewModel.observeTyped(owner, Observer { observed = it })

        viewModel.emitTyped("typed-value")

        assertEquals("typed-value", observed)
    }

    @Test
    fun `when typed channel is observed from controller contract then typed value is delivered`() {
        val owner = lifecycleOwner()
        var observed: String? = null
        val controller: Controller = viewModel

        controller.observe(viewModel.typedChannel, owner, Observer { observed = it })

        viewModel.emitTyped("controller-value")

        assertEquals("controller-value", observed)
    }

    private fun lifecycleOwner(): LifecycleOwner {
        val owner: LifecycleOwner = mock()
        val lifecycle = LifecycleRegistry(owner).apply {
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
        whenever(owner.lifecycle).thenReturn(lifecycle)
        return owner
    }

    private class SampleViewModel : BaseViewModel() {
        val typedChannel = channel<String>("typed")

        fun emitLegacy(value: String) {
            postValue("legacy", ValueOutput(value))
        }

        fun observeTyped(owner: LifecycleOwner, observer: Observer<String>) {
            observe(typedChannel, owner, observer)
        }

        fun emitTyped(value: String) {
            postValue(typedChannel, value)
        }
    }
}
