package dev.reimer.latex.gradle.plugin

import java.io.File

data class Test(
    val directory: File,
    val description: String,
    val configuration: Configuration,
    val expectation: Expectation
)
