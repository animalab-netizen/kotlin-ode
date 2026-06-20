package br.com.lab.kotlin.ode.business.dto

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class OutputTest {
    @Test
    fun `when output is empty then it is empty and has no value`() {
        val output = EmptyOutput<String>()

        assertTrue(output.isEmpty())
        assertFalse(output.isSuccess())
        assertFalse(output.isError())
        assertFalse(output.hasValue())
    }

    @Test
    fun `when output has value then it is successful and not empty`() {
        val output = ValueOutput("value")

        assertFalse(output.isEmpty())
        assertTrue(output.isSuccess())
        assertFalse(output.isError())
        assertTrue(output.hasValue())
    }

    @Test
    fun `when output has error then it is not empty and not successful`() {
        val output = ErrorOutput<String>(RuntimeException())

        assertFalse(output.isEmpty())
        assertFalse(output.isSuccess())
        assertTrue(output.isError())
        assertFalse(output.hasValue())
    }
}
