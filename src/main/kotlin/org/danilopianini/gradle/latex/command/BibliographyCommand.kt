package org.danilopianini.gradle.latex.command

import org.danilopianini.gradle.latex.configuration.BibliographyConfiguration
import org.gradle.process.internal.ExecAction

interface BibliographyCommand {
    fun execute(action: ExecAction, configuration: BibliographyConfiguration)
}