package org.danilopianini.gradle.latex.configuration

import org.gradle.api.file.RegularFileProperty

interface BibliographyConfiguration : BibliographyCommandConfiguration {

    val aux: RegularFileProperty

    /**
     * Represents bib file used to call bibtex.
     */
    val bib: RegularFileProperty

    val bbl: RegularFileProperty
}