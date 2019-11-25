package dev.reimer.latex.gradle.plugin.configuration

import dev.reimer.latex.gradle.plugin.command.BibliographyCommand
import org.gradle.api.provider.Property

interface BibliographyCommandConfiguration : CommandConfiguration {

    val bibliographyCommand: Property<BibliographyCommand>
}