package dev.reimer.tex.gradle.plugin.compiler.bibliography

import dev.reimer.tex.gradle.plugin.directoryProperty
import dev.reimer.tex.gradle.plugin.fileProperty
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileNotFoundException

abstract class DefaultBibliographyCompiler internal constructor(
    private val command: String,
    private val copier: BibliographyCopier
) : DefaultTask(), BibliographyCompiler {

    private companion object {
        val CITATION_REGEX = Regex("""^\\citation""")
    }

    final override val auxFile = project.fileProperty()

    final override val texDir = project.directoryProperty()

    @TaskAction
    final override fun compile() {
        val aux = auxFile.get().asFile
        if (!aux.exists()) {
            throw GradleException("${aux.absolutePath} does not exist.", FileNotFoundException())
        }

        val containsCitations = aux.useLines { lines ->
            lines.any { line ->
                CITATION_REGEX.containsMatchIn(line)
            }
        }
        if (!containsCitations) {
            didWork = false
            return
        }

        // Copy bibliography resources to build folder.
        val sourceDir = texDir.get().asFile
        val resources = copier.findResources(aux)
        resources.forEach { file ->
            sourceDir.resolve(file).takeIf(File::exists)?.copyTo(aux.resolveSibling(file))
        }

        project.exec { spec ->
            spec.workingDir = aux.parentFile
            spec.executable = command
            spec.args(aux.nameWithoutExtension)
        }
        didWork = true
    }
}