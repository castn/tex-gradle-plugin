package org.danilopianini.gradle.latex.test

import com.uchuhimo.konf.ConfigSpec
import java.io.File

data class Test(
    val directory: File,
    val description: String,
    val configuration: Configuration,
    val expectation: Expectation
) {
    companion object : ConfigSpec() {
        val description by required<String>()
        val configuration by required<Configuration>()
        val expectation by required<Expectation>()
    }
}
