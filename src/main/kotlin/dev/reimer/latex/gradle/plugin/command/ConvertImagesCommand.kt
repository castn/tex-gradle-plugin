package dev.reimer.latex.gradle.plugin.command

import dev.reimer.latex.gradle.plugin.configuration.ConvertImagesConfiguration
import org.gradle.process.internal.ExecAction

interface ConvertImagesCommand {
    fun execute(action: ExecAction, configuration: ConvertImagesConfiguration)
}