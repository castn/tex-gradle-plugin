package dev.reimer.latex.gradle.plugin.internal

import dev.reimer.latex.gradle.plugin.configuration.LatexTaskConfiguration
import org.gradle.api.file.RegularFile
import java.io.File

internal val LatexTaskConfiguration.auxDirFile: File
    get() = auxDirectory.get().asFile

internal val LatexTaskConfiguration.aux: RegularFile
    get() = auxDirectory.get().file("${texNameWithoutExtension}.${FileExtensions.AUX}")

internal val LatexTaskConfiguration.auxFile: File
    get() = aux.asFile

internal val LatexTaskConfiguration.texFile: File
    get() = tex.get().asFile

internal val LatexTaskConfiguration.texDir: File
    get() = texFile.parentFile

internal val LatexTaskConfiguration.texNameWithoutExtension: String
    get() = texFile.nameWithoutExtension

internal val LatexTaskConfiguration.bibFile: File?
    get() = bib.orNull?.asFile

internal val LatexTaskConfiguration.hasBibFile: Boolean
    get() = bibFile?.exists() == true
