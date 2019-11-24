package org.danilopianini.gradle.latex.command

import org.danilopianini.gradle.latex.configuration.ConvertImagesConfiguration
import org.gradle.process.internal.ExecAction

interface ConvertImagesCommand {
    fun execute(action: ExecAction, configuration: ConvertImagesConfiguration)
}