package br.com.lab.kotlin.ode.business.dto

data class ErrorOutput<out V>(
    override val error: Throwable?,
    override val value: V? = null
) : Output<V>()
