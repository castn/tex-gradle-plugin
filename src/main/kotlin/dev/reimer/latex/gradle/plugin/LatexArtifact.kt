package dev.reimer.latex.gradle.plugin

import dev.reimer.latex.gradle.plugin.configuration.BibliographyConfiguration
import dev.reimer.latex.gradle.plugin.configuration.ConvertImagesConfiguration
import dev.reimer.latex.gradle.plugin.configuration.ExtensionConfiguration
import dev.reimer.latex.gradle.plugin.configuration.PdfConfiguration
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import java.io.File

/**
 * Represents a TeX artifact which will can be compiled into a PDF.
 * Used to maintain a graph of dependencies.
 */
class LatexArtifact internal constructor(
    val project: Project,
    val name: String
) : ExtensionConfiguration, BibliographyConfiguration, ConvertImagesConfiguration, PdfConfiguration {

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

    override val bibliographyCommand =
        project.propertyWithDefault(latexExtension.bibliographyCommand)

    override val pdfCommand =
        project.propertyWithDefault(latexExtension.pdfCommand)

    override val convertImagesCommand =
        project.propertyWithDefault(latexExtension.convertImagesCommand)

    override val quiet =
        project.propertyWithDefault(latexExtension.quiet)

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

    internal val taskNames = LatexArtifactTaskNames(this)

    private val latexExtension: LatexExtension
        get() = project.extensions[Latex.EXTENSION_NAME] as LatexExtension
}