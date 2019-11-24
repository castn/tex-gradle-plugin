package org.danilopianini.gradle.latex.configuration

import org.danilopianini.gradle.latex.command.BibliographyCommand
import org.gradle.api.provider.Property

interface BibliographyCommandConfiguration : CommandConfiguration {

    val bibliographyCommand: Property<BibliographyCommand>
}