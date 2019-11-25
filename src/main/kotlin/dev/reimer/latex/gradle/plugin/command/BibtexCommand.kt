package dev.reimer.latex.gradle.plugin.command

import dev.reimer.latex.gradle.plugin.Latex
import dev.reimer.latex.gradle.plugin.configuration.BibliographyConfiguration
import org.gradle.api.GradleException
import org.gradle.process.internal.ExecAction

object BibtexCommand : BibliographyCommand {
    private const val bibtex = "bibtex"

    override fun execute(action: ExecAction, configuration: BibliographyConfiguration) {


        val aux = configuration.aux.get().asFile
        if (!aux.exists()) {
            throw GradleException("${aux.absolutePath} does not exist, cannot invoke BibTeX.")
        }
        val containsCitations = aux.useLines { lines ->
            lines.any { line ->
                line.contains("""\citation""")
            }
        }
        if (containsCitations) {
            action.executable = bibtex
            action.args(aux.path)
            action.execute()
        } else {
            Latex.LOG.info("No citation in ${aux.absolutePath}, BibTeX not invoked.")
        }
    }
}