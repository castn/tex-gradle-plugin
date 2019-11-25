package org.danilopianini.gradle.latex.test

data class Expectation(
    val file_exists: List<String>,
    val success: List<String>,
    val failure: List<String>
)
