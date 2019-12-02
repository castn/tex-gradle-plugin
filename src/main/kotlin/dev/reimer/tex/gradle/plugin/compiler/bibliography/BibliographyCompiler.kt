package dev.reimer.tex.gradle.plugin.compiler.bibliography

import org.gradle.api.Task
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile

/**
 * Compile bibliography referenced from a single [auxFile].
 * Paths to bibliography files are resolved relative to [texDir],
 * which should be the TeX file's parent directory.
 */
interface BibliographyCompiler : Task {

    @get:InputFile
    val auxFile: RegularFileProperty

    // TODO Mark the generated bbl file as output.
    // TODO Instead, mark the real bib file as input. That can be guessed from the aux file.
    @get:InputDirectory
    val texDir: DirectoryProperty

    fun compile()
}