package dev.reimer.latex.gradle.plugin.command

import dev.reimer.latex.gradle.plugin.configuration.PdfConfiguration
import org.gradle.process.internal.ExecAction

interface PdfCommand {
    fun execute(action: ExecAction, configuration: PdfConfiguration)
}