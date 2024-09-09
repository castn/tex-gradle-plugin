package dev.reimer.tex.gradle.plugin.compiler.bibliography

import org.gradle.api.Task
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile

/**
 * Compile bibliography referenced from the [buildDir].
 * Paths to bibliography files are resolved relative to [sourceDir],
 * the TeX file's parent directory.
 */
interface BibliographyCompiler : Task {

    @get:Input
    val jobName: Property<String>

    @get:InputDirectory
    val buildDir: DirectoryProperty

    @get:InputDirectory
    val sourceDir: DirectoryProperty

    @get:Input
    val quiet: Property<Boolean>

    @get:OutputFile
    val destination: Provider<RegularFile>

    fun compile()
}