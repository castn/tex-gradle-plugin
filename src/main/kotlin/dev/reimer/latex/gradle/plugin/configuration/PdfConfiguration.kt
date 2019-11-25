package dev.reimer.latex.gradle.plugin.configuration

import org.gradle.api.file.RegularFileProperty

interface PdfConfiguration : PdfCommandConfiguration {

    /**
     * Represents tex file which is used to call bibtex, pdflatex
     * Must be set.
     */
    val tex: RegularFileProperty

    val pdf: RegularFileProperty
}