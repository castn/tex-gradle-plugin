package org.danilopianini.gradle.latex.command

import org.danilopianini.gradle.latex.configuration.PdfConfiguration
import org.gradle.process.internal.ExecAction

interface PdfCommand {
    fun execute(action: ExecAction, configuration: PdfConfiguration)
}