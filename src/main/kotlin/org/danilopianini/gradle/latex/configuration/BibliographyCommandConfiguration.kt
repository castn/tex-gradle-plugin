package org.danilopianini.gradle.latex.configuration

import org.gradle.api.provider.Property

interface BibliographyCommandConfiguration {

    val bibliographyCommand: Property<String>
}