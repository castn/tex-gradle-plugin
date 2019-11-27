package dev.reimer.latex.gradle.plugin.command

import dev.reimer.latex.gradle.plugin.configuration.LatexTaskConfiguration
import org.gradle.process.ExecSpec

object PdflatexCommand : Command {

    override fun ExecSpec.configure(configuration: LatexTaskConfiguration): Boolean {
        executable = "pdflatex"
        if (configuration.quiet.get()) args("-quiet")
        // Halt immediately on errors.
        args("-halt-on-error", "-interaction=nonstopmode")
        args("-job-name=${configuration.jobName.get()}")
        args("-output-directory=${configuration.outputDirectory.get().asFile.absolutePath}")
        args("-aux-directory=${configuration.auxDirectory.get().asFile.absolutePath}")
        args(configuration.tex.get().asFile.absolutePath)
        return true
    }
}