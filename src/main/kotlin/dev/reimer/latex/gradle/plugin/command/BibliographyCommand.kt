package dev.reimer.latex.gradle.plugin.command

import dev.reimer.latex.gradle.plugin.configuration.BibliographyConfiguration
import org.gradle.process.internal.ExecAction

interface BibliographyCommand {
    fun execute(action: ExecAction, configuration: BibliographyConfiguration)
}