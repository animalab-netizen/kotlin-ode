package br.com.lab.kotlin.ode.gateway.mvvm

interface ControllerFactory<V,T: Controller> {
    fun create(context: V): T
}