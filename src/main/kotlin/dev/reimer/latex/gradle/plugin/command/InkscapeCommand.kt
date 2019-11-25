package dev.reimer.latex.gradle.plugin.command

import dev.reimer.latex.gradle.plugin.configuration.ConvertImagesConfiguration
import org.gradle.process.internal.ExecAction

object InkscapeCommand : ConvertImagesCommand {
    private const val inkscape = "inkscape"

    override fun execute(action: ExecAction, configuration: ConvertImagesConfiguration) {
        action.executable = inkscape
        TODO("not implemented")
        action.execute()
    }
}