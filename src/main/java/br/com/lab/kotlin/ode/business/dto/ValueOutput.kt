package br.com.lab.kotlin.ode.business.dto

data class ValueOutput<out V>(override val value: V? = null) : Output<V>()
