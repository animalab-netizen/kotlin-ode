package br.com.lab.kotlin.ode.gateway.mvvm

/**
 * Main MVVM bridge for kotlin-ode.
 *
 * Public API intent:
 * - subclass from app/viewmodel code
 * - create typed channels through [channel]
 * - dispatch business flows through [dispatchUseCase]
 * - observe values through [Controller.observe]
 *
 * Internal runtime details such as channel registry enumeration and explicit disposal are kept
 * module-scoped to reduce accidental coupling in consumers.
 */

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import br.com.lab.kotlin.ode.business.dto.Output
import br.com.lab.kotlin.ode.business.interactor.CallbackDecorator
import br.com.lab.kotlin.ode.business.interactor.CompositeJobDisposable
import br.com.lab.kotlin.ode.business.interactor.UseCase
import br.com.lab.kotlin.ode.business.interactor.UseCaseDispatcher
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel(), Controller {
    /**
     * Typed observation key that should be preferred over raw string channels in new code.
     */
    data class Channel<T>(val name: String)

    private val channels: MutableMap<String, MutableLiveData<Any?>> = mutableMapOf()
    private val compositeJobDisposable = CompositeJobDisposable()

    final override fun observe(
        channelName: String,
        owner: LifecycleOwner,
        listener: Observer<Any?>
    ) {
        getOrCreateChannel(channelName).observe(owner, listener)
    }

    final override fun <T> observe(
        channel: Channel<T>,
        owner: LifecycleOwner,
        listener: Observer<T>
    ) {
        @Suppress("UNCHECKED_CAST")
        getOrCreateChannel(channel.name).observe(owner, listener as Observer<Any?>)
    }

    internal fun getChannels(): List<String> {
        return channels.keys.toList()
    }

    internal fun disposeAll() {
        compositeJobDisposable.cancel()
    }

    protected fun <T> channel(name: String): Channel<T> = Channel(name)

    protected open fun postValue(channelName: String, value: Any?) {
        getOrCreateChannel(channelName).postValue(value)
    }

    protected open fun <T> postValue(channel: Channel<T>, value: T) {
        getOrCreateChannel(channel.name).postValue(value)
    }

    protected open fun <P, R> dispatchUseCase(
        param: P?,
        useCase: UseCase<P, R>,
        listener: (Output<R>) -> Unit
    ): Job {
        val dispatcher = UseCaseDispatcher(CallbackDecorator(useCase, listener))
        val job = dispatcher.dispatch(param)
        compositeJobDisposable.add(job)
        return job
    }

    override fun onCleared() {
        disposeAll()
        super.onCleared()
    }

    private fun getOrCreateChannel(channelName: String): MutableLiveData<Any?> {
        return channels.getOrPut(channelName) { MutableLiveData() }
    }
}
