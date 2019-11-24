package org.danilopianini.gradle.latex

import org.danilopianini.gradle.latex.configuration.BibliographyTaskConfiguration
import org.danilopianini.gradle.latex.configuration.ConvertImagesTaskConfiguration
import org.danilopianini.gradle.latex.configuration.PdfTaskConfiguration
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.get
import java.io.File

/**
 * Represents a TeX artifact which will can be compiled into a PDF.
 * Used to maintain a graph of dependencies.
 */
class LatexArtifact internal constructor(
    val project: Project,
    val name: String
) : BibliographyTaskConfiguration, ConvertImagesTaskConfiguration,
    PdfTaskConfiguration {

    private fun fromName(extension: String): String {
        return when {
            name.endsWith(".tex") -> {
                when (extension) {
                    "tex" -> name
                    else -> name.substringAfterLast('.') + extension
                }
            }
            else -> "$name.$extension"
        }
    }

    override val bibliographyCommand: Property<String> = project.propertyWithDefault(latexExtension.bibliographyCommand)

    override val pdfCommand: Property<String> = project.propertyWithDefault(latexExtension.pdfCommand)

    override val pdfQuiet: Property<Boolean> = project.propertyWithDefault(latexExtension.pdfQuiet)

    override val pdfArguments = project.listPropertyWithDefault(latexExtension.pdfArguments)

    override val convertImagesCommand: Property<String> =
        project.propertyWithDefault(latexExtension.convertImagesCommand)

    override val tex = project.filePropertyWithDefault {
        project.file(fromName("tex"))
    }

    override val aux = project.filePropertyWithDefault {
        project.file(fromName("aux"))
    }

    override val bbl = project.filePropertyWithDefault {
        project.file(fromName("bbl"))
    }

    override val pdf = project.filePropertyWithDefault {
        project.file(fromName("pdf"))
    }

    override val images = project.objects.fileCollection()

    override val bib = project.filePropertyWithDefault {
        project.file(fromName("bib")).takeIf(File::exists)
    }

    /**
     * Collection of dependencies which have to be compiled
     * in order for this one to work (e.g. used with \input).
     */
    val dependsOn = project.setPropertyWithDefault { emptySet<LatexArtifact>() }

    internal val hasBib get() = bib.isPresent

    internal val taskSuffix = name
        .removeSuffix(".tex")
        .split(File.pathSeparatorChar, '-', '.')
        .joinToString("", transform = String::capitalize)

    private val latexExtension: LatexExtension
        get() = project.extensions[Latex.EXTENSION_NAME] as LatexExtension
}