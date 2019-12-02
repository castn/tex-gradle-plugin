package dev.reimer.tex.gradle.plugin.compiler.image

import org.gradle.api.Task
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile

/**
 * Convert a single image to a format, that is readable by a TeX compiler.
 * The converted file is saved to the [destinationDir] with the same name,
 * but not necessarily the same file extension.
 */
interface ImageConverter : Task {

    @get:InputFile
    val image: RegularFileProperty

    // TODO Instead, mark the real converted image file as output.
    @get:InputDirectory
    val destinationDir: DirectoryProperty

    fun convert()
}