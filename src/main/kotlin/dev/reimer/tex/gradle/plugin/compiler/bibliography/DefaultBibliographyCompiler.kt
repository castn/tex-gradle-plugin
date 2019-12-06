package dev.reimer.tex.gradle.plugin.compiler.bibliography

import dev.reimer.tex.gradle.plugin.directoryProperty
import dev.reimer.tex.gradle.plugin.internal.FileExtensions.AUX
import dev.reimer.tex.gradle.plugin.internal.FileExtensions.BBL
import dev.reimer.tex.gradle.plugin.property
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class DefaultBibliographyCompiler internal constructor() : DefaultTask(), BibliographyCompiler {

    @get:Input
    protected abstract val command: String

    final override val jobName = project.property<String>()

    final override val buildDir = project.directoryProperty()

    final override val sourceDir = project.directoryProperty()

    private val destinationName: Provider<String> = jobName.map { "$it.${BBL}" }

    override val destination: Provider<RegularFile> =
        project.layout.file(buildDir.map { it.asFile.resolve(destinationName.get()) })

    private val auxFileName: Provider<String> = jobName.map { "$it.${AUX}" }

    @get:InputFile
    protected val auxFile: Provider<RegularFile> =
        project.layout.file(buildDir.map { it.asFile.resolve(auxFileName.get()) })

    /**
     * Files that need to be copied from the source dir to the build dir,
     * for the bibliography compiler to work.
     */
    @get:InputFiles
    protected abstract val resources: Provider<Iterable<File>>

    @get:Input
    protected abstract val containsCitations: Provider<Boolean>

    @TaskAction
    final override fun compile() {
        println(containsCitations.get())
        println(resources.get().joinToString())
        if (!containsCitations.get()) {
            didWork = false
            return
        }

        // Copy bibliography resources to build folder.
        val sourceDir = sourceDir.get().asFile
        val buildDir = buildDir.get().asFile
        val resources = resources.get()
        resources.forEach { file ->
            logger.quiet("Copying ${file.path} to build directory.")
            sourceDir.resolve(file).takeIf(File::exists)?.copyTo(buildDir.resolve(file))
        }

        project.exec { spec ->
            spec.workingDir = buildDir
            spec.executable = command
            spec.args(jobName.get())
        }
        didWork = true
    }
}