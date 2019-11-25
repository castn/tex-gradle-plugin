package dev.reimer.latex.gradle.plugin.command

import dev.reimer.latex.gradle.plugin.configuration.ConvertImagesConfiguration
import org.gradle.process.internal.ExecAction

object NopConvertImagesCommand : ConvertImagesCommand {
    override fun execute(action: ExecAction, configuration: ConvertImagesConfiguration) {}
}