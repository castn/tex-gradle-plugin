package dev.reimer.latex.gradle.plugin.command

import dev.reimer.latex.gradle.plugin.configuration.LatexTaskConfiguration
import org.gradle.process.ExecSpec
import java.io.Serializable

interface Command : Serializable {
    fun ExecSpec.configure(configuration: LatexTaskConfiguration): Boolean
}