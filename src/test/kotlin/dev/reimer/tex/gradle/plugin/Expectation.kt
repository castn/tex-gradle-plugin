package dev.reimer.tex.gradle.plugin

data class Expectation(
    val file_exists: List<String>,
    val success: List<String>,
    val failure: List<String>
)
