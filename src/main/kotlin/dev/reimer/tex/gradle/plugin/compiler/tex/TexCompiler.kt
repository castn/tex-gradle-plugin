package dev.reimer.tex.gradle.plugin.compiler.tex

import org.gradle.api.Task
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile

/**
 * Compile a single TeX file.
 */
interface TexCompiler : Task {

    @get:InputFile
    val tex: RegularFileProperty

    @get:Input
    val quiet: Property<Boolean>

    @get:Input
    val overwrite: Property<Boolean>

    @get:Internal
    val destinationDir: DirectoryProperty

    @get:OutputFile
    val destination: Provider<RegularFile>

    @get:Internal
    val buildDir: DirectoryProperty

    fun compileTex()
}