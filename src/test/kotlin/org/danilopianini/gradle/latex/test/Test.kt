package org.danilopianini.gradle.latex.test

import java.io.File

data class Test(
    val directory: File,
    val description: String,
    val configuration: Configuration,
    val expectation: Expectation
)
