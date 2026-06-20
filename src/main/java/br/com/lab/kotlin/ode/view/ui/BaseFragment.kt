package br.com.lab.kotlin.ode.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.lab.kotlin.ode.business.dto.ErrorOutput
import br.com.lab.kotlin.ode.business.dto.Output
import br.com.lab.kotlin.ode.business.dto.ValueOutput
import br.com.lab.kotlin.ode.business.exception.AuthenticationException
import br.com.lab.kotlin.ode.business.exception.HttpException
import br.com.lab.kotlin.ode.business.exception.InternetConnectionException
import br.com.lab.kotlin.ode.gateway.mvvm.BaseViewModel
import br.com.lab.kotlin.ode.gateway.mvvm.Controller

abstract class BaseFragment<C : Controller> : Fragment() {
    protected var hideToolbar = false

    protected val controller by lazy { setupController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        if (hideToolbar) (activity as AppCompatActivity).supportActionBar?.hide()
        registerObserveChannel()
    }

    private fun registerObserveChannel() = observe(channelName(), ::handleResult)

    private fun handleResult(value: Any?) {
        when (value) {
            is ErrorOutput<*> -> handleThrowable(value.error)
            is ValueOutput<*> -> handleSuccess(value.value)
            is Output<*> -> handleOutput(value)
        }
    }

    protected abstract fun setupViews(view: View)

    protected abstract fun channelName(): String

    protected abstract fun setupController(): C

    protected abstract fun getLayout(): Int

    protected open fun handleOutput(output: Output<*>) {
        when (output) {
            is ErrorOutput<*> -> handleThrowable(output.error)
            is ValueOutput<*> -> handleSuccess(output.value)
            else -> Unit
        }
    }

    private fun handleThrowable(error: Throwable?) {
        when (error) {
            is AuthenticationException -> handleAuthError()
            is HttpException -> handleHttpError(error)
            is InternetConnectionException -> handleConnectionError()
            else -> handleError(error)
        }
    }

    protected open fun handleAuthError() {}

    protected open fun handleHttpError(error: HttpException) {}

    protected open fun handleConnectionError() {}

    protected open fun handleError(error: Throwable?) {}

    protected open fun handleSuccess(value: Any?) {}

    protected fun observe(channelName: String, listener: Observer<Any?>) {
        controller.observe(channelName, this, listener)
    }

    protected fun <T> observe(channel: BaseViewModel.Channel<T>, listener: Observer<T>) {
        controller.observe(channel, this, listener)
    }

    protected fun setupToolbar(toolbar: Toolbar, homeAsUpEnabled: Boolean = false) {
        (activity as? BaseActivity)?.resetToolbar(toolbar, homeAsUpEnabled)
    }
}
