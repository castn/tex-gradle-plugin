package dev.reimer.tex.gradle.plugin.compiler.image

import dev.reimer.tex.gradle.plugin.directoryProperty
import dev.reimer.tex.gradle.plugin.fileProperty
import dev.reimer.tex.gradle.plugin.withExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class DefaultInkscape internal constructor(
    private val exportType: String
) : DefaultTask(), ImageConverter {

    private companion object {
        private val INKSCAPE_READABLE_EXTENSIONS = listOf("svg", "emf")
    }

    final override val image = project.fileProperty()

    final override val destinationDir = project.directoryProperty()

    @TaskAction
    final override fun convert() {
        val image = image.get().asFile

        if (!image.isFile) return
        if (!image.exists()) return
        if (!image.isInkscapeReadable) return
        if (!image.needsConversion()) return

        val output = destinationDir.get()
            .asFile
            .resolve("${image.nameWithoutExtension}.$exportType")

        project.exec {
            it.executable = "inkscape"
            it.args("--without-gui")
            it.args("--export-type=\"$exportType\"")
            it.args("--export-file=\"$output\"")
            it.args(image.absolutePath)
        }
    }

    private fun File.needsConversion() = !withExtension(exportType).exists()

    private val File.isInkscapeReadable
        get() = extension in INKSCAPE_READABLE_EXTENSIONS
}