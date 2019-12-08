package dev.reimer.tex.gradle.plugin.task

import dev.reimer.tex.gradle.plugin.TexPlugin
import dev.reimer.tex.gradle.plugin.compiler.CompilerFactory
import dev.reimer.tex.gradle.plugin.directoryProperty
import dev.reimer.tex.gradle.plugin.internal.TexResourcesFileFilter
import dev.reimer.tex.gradle.plugin.property
import dev.reimer.tex.gradle.plugin.texExtension
import org.gradle.api.file.RelativePath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileFilter

/**
 * Generate TeX compatible resources.
 */
open class TexResources : FilteredSourceTask() {

    @Internal
    override fun getGroup() = TexPlugin.TASK_GROUP

    @Internal
    override fun getDescription() = "Processes TeX resources."

    @get:Input
    val imageConverter = project.property(texExtension.imageConverter)

    @get:OutputDirectory
    val destinationDir = project.directoryProperty()

    override val fileFilter: FileFilter = TexResourcesFileFilter(project)

    @TaskAction
    fun generate() {
        makeDirs()
        convertImages()
    }

    private fun makeDirs() {
        destinationDir.get().asFile.mkdirs()
    }

    private fun convertImages() {
        source.visit { element ->
            convertImage(element.file, element.relativePath)
        }
    }

    private fun convertImage(file: File, path: RelativePath) {
        val outputDirectory: File = destinationDir.get()
            .asFile
            .resolve(path.pathString)
            .parentFile
        CompilerFactory.createImageConverter(project, imageConverter.get()).apply {
            image.set(file)
            destinationDir.set(outputDirectory)
            convert()
        }
    }
}