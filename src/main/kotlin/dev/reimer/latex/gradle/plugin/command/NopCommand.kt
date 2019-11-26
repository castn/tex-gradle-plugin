package dev.reimer.latex.gradle.plugin.command

import dev.reimer.latex.gradle.plugin.configuration.LatexTaskConfiguration
import org.gradle.process.ExecSpec

object NopCommand : Command {
    override fun ExecSpec.configure(configuration: LatexTaskConfiguration) = false
}