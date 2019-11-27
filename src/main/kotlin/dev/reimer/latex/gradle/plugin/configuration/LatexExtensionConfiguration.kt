package dev.reimer.latex.gradle.plugin.configuration

import dev.reimer.latex.gradle.plugin.command.Command
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

interface LatexExtensionConfiguration {

    val bibliographyCommand: Property<Command>

    val convertImagesCommand: Property<Command>

    val pdfCommand: Property<Command>

    val quiet: Property<Boolean>

    val overwrite: Property<Boolean>

    val outputDirectory: DirectoryProperty

    val auxDirectory: DirectoryProperty

    /**
     * Collection of image files or directories with images
     * which have to be transformed because LaTeX cannot use them directly (e.g. svg, emf).
     * These are transformed to PDFs which then can be included in pdflatex.
     */
    val images: ConfigurableFileCollection
}