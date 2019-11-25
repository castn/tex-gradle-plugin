package org.danilopianini.gradle.latex.command

import org.danilopianini.gradle.latex.configuration.PdfConfiguration
import org.gradle.process.internal.ExecAction

object DockerPdflatexCommand : PdfCommand {
    private const val pdflatex = "pdflatex"

    override fun execute(action: ExecAction, configuration: PdfConfiguration) {
        action.executable = pdflatex
        if (configuration.quiet.get()) {
            action.args("-quiet")
        }
        action.args("-shell-escape", "-synctex=1", "-interaction=nonstopmode", "-halt-on-error")
        action.args(configuration.tex.get().asFile.absolutePath)
        action.execute()
        action.execute()
    }
}