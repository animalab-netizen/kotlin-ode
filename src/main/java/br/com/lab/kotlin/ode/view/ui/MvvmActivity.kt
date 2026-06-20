package br.com.lab.kotlin.ode.view.ui

import br.com.lab.kotlin.ode.gateway.mvvm.Controller

abstract class MvvmActivity<C: Controller>: BaseActivity() {
    protected val controller by lazy { setupController() }

    protected abstract fun setupController(): C
}