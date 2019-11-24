package org.danilopianini.gradle.latex

import org.danilopianini.gradle.latex.configuration.BibtexTaskConfiguration
import org.danilopianini.gradle.latex.configuration.ConvertImagesTaskConfiguration
import org.danilopianini.gradle.latex.configuration.PdflatexTaskConfiguration
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
) : BibtexTaskConfiguration, ConvertImagesTaskConfiguration,
    PdflatexTaskConfiguration {

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

    override val bibtexCommand: Property<String> = project.propertyWithDefault(latexExtension.bibtexCommand)

    override val pdflatexCommand: Property<String> = project.propertyWithDefault(latexExtension.pdflatexCommand)

    override val pdflatexQuiet: Property<Boolean> = project.propertyWithDefault(latexExtension.pdflatexQuiet)

    override val pdflatexArguments = project.listPropertyWithDefault(latexExtension.pdflatexArguments)

    override val inkscapeCommand: Property<String> = project.propertyWithDefault(latexExtension.inkscapeCommand)

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