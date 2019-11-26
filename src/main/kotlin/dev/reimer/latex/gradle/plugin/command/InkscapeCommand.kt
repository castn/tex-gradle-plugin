package dev.reimer.latex.gradle.plugin.command

import dev.reimer.latex.gradle.plugin.configuration.LatexTaskConfiguration
import org.gradle.process.ExecSpec

object InkscapeCommand : Command {

    override fun ExecSpec.configure(configuration: LatexTaskConfiguration): Boolean {
        executable = "inkscape"
        TODO("not implemented")
        return true
    }
}