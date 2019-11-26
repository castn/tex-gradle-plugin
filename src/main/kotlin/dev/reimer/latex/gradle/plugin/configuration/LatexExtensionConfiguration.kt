package dev.reimer.latex.gradle.plugin.configuration

import dev.reimer.latex.gradle.plugin.command.Command
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

interface LatexExtensionConfiguration {

    val bibliographyCommand: Property<Command>

    val convertImagesCommand: Property<Command>

    val pdfCommand: Property<Command>

    val quiet: Property<Boolean>

    val overwrite: Property<Boolean>

    val auxDirectory: DirectoryProperty
}