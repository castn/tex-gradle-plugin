package dev.reimer.latex.gradle.plugin.command

import dev.reimer.latex.gradle.plugin.configuration.PdfConfiguration
import org.gradle.process.internal.ExecAction

object PdflatexCommand : PdfCommand {
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