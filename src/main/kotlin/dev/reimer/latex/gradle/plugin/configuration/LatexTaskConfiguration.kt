package dev.reimer.latex.gradle.plugin.configuration

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property

interface LatexTaskConfiguration : LatexExtensionConfiguration {

    /**
     * Represents tex file which is used to call bibtex, pdflatex
     * Must be set.
     */
    val tex: RegularFileProperty

    val jobName: Property<String>

    val pdf: RegularFileProperty

    val bib: RegularFileProperty
}