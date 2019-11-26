package dev.reimer.latex.gradle.plugin

import java.io.File

interface Test {
    val directory: File
    val description: String
    val configuration: Configuration
    val expectation: Expectation
    val buildFile: String
}
