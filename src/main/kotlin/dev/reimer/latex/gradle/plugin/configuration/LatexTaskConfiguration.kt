package dev.reimer.latex.gradle.plugin.configuration

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
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

    val outputDirectory: DirectoryProperty

    /**
     * Collection of image files or directories with images
     * which have to be transformed because LaTeX cannot use them directly (e.g. svg, emf).
     * These are transformed to PDFs which then can be included in pdflatex.
     */
    val images: ConfigurableFileCollection
}