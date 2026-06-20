package br.com.lab.kotlin.ode.gateway.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface Controller {
    fun observe(channelName: String, owner: LifecycleOwner, listener: Observer<Any?>)

    fun <T> observe(
        channel: BaseViewModel.Channel<T>,
        owner: LifecycleOwner,
        listener: Observer<T>
    )
}
