package br.com.lab.kotlin.ode.business.dto

sealed class Output<out V> {
    open val value: V? = null
    open val error: Throwable? = null

    fun isError(): Boolean = this is ErrorOutput

    fun isSuccess(): Boolean = this is ValueOutput<*>

    fun isEmpty(): Boolean = this is EmptyOutput<*>

    fun hasValue(): Boolean = value != null
}
