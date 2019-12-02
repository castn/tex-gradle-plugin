package dev.reimer.tex.gradle.plugin

import dev.reimer.tex.gradle.plugin.compiler.bibliography.BibliographyCompilerType
import dev.reimer.tex.gradle.plugin.compiler.image.ImageConverterType
import dev.reimer.tex.gradle.plugin.compiler.tex.TexCompilerType
import org.gradle.api.Project
import org.gradle.api.provider.Property

/**
 * Gradle extension to create new dynamic tasks & maintain and manage latex artifacts.
 * Registered to Gradle as extension in LatexPlugin. Thereafter the instance can be accessed via project.latex
 *
 */
open class TexExtension(
    project: Project
) : TexScope {

    final override val texCompiler = project.property { TexCompilerType.PDFLATEX }

    final override val bibliographyCompiler = project.property { BibliographyCompilerType.BIBTEX }

    final override val imageConverter = project.property { ImageConverterType.INKSCAPE_PDF }

    final override val quiet: Property<Boolean> = project.property { true }

    final override val overwrite: Property<Boolean> = project.property { true }
}
