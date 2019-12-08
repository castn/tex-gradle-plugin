package dev.reimer.tex.gradle.plugin

import dev.reimer.tex.gradle.plugin.compiler.bibliography.BibliographyCompilerType
import dev.reimer.tex.gradle.plugin.compiler.image.ImageConverterType
import dev.reimer.tex.gradle.plugin.compiler.tex.TexCompilerType
import org.gradle.api.provider.Property

interface TexScope {

    val bibliographyCompiler: Property<BibliographyCompilerType>

    val texCompiler: Property<TexCompilerType>

    val imageConverter: Property<ImageConverterType>

    val quiet: Property<Boolean>
}
