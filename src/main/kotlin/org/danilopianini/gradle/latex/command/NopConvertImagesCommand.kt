package org.danilopianini.gradle.latex.command

import org.danilopianini.gradle.latex.configuration.ConvertImagesConfiguration
import org.gradle.process.internal.ExecAction

object NopConvertImagesCommand : ConvertImagesCommand {
    override fun execute(action: ExecAction, configuration: ConvertImagesConfiguration) {}
}