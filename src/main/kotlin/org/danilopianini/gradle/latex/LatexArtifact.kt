package org.danilopianini.gradle.latex

import org.gradle.api.Project
import java.io.File

/**
 * Represents a TeX artifact which will can be compiled into a PDF.
 * Used to maintain a graph of dependencies.
 *
 */
class LatexArtifact internal constructor(
    val project: Project,
    val name: String
) {

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

    /**
     * Represents tex file which is used to call bibtex, pdflatex
     * Must be set.
     */
    val tex = project.filePropertyWithDefault {
        project.file(fromName("tex"))
    }

    val aux = project.filePropertyWithDefault {
        project.file(fromName("aux"))
    }

    val bbl = project.filePropertyWithDefault {
        project.file(fromName("bbl"))
    }

    val pdf = project.filePropertyWithDefault {
        project.file(fromName("pdf"))
    }

    /**
     * Represents bib file used to call bibtex.
     */
    val bib = project.filePropertyWithDefault {
        project.file(fromName("bib")).takeIf(File::exists)
    }

    /**
     * Collection of dependencies which have to be compiled
     * in order for this one to work (e.g. used with \input).
     */
    val dependsOn = project.setPropertyWithDefault { emptySet<LatexArtifact>() }

    /**
     * Collection of image files or directories with images
     * which have to be transformed because LaTeX cannot use them directly (e.g. svg, emf).
     * These are transformed to PDFs which then can be included in pdflatex.
     */
    val imageFiles = project.setPropertyWithDefault { emptySet<File>() }

    /**
     * Extra arguments to be passed to pdflatex when building this artifact.
     */
    val extraArgs = project.listPropertyWithDefault {
        listOf("-shell-escape", "-synctex=1", "-interaction=nonstopmode", "-halt-on-error")
    }

    /**
     * Differential documents to get produced.
     */
    val diffs = project.listPropertyWithDefault { emptyList<Int>() }

    val quiet = project.propertyWithDefault {
        project.extensions.findByType(LatexExtension::class.java)?.quiet?.get() ?: true
    }

    private fun fileFromName(extension: String) = project.file(fromName(extension))

    internal val hasBib get() = bib.isPresent

    internal val taskSuffix = name
        .removeSuffix(".tex")
        .split(File.pathSeparatorChar, '-', '.')
        .joinToString("", transform = String::capitalize)
}