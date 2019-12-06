package dev.reimer.tex.gradle.plugin.compiler.bibliography

import dev.reimer.tex.gradle.plugin.directoryProperty
import dev.reimer.tex.gradle.plugin.fileProperty
import dev.reimer.tex.gradle.plugin.internal.FileExtensions.BIB
import dev.reimer.tex.gradle.plugin.internal.FileExtensions.BST
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileNotFoundException

abstract class DefaultBibliographyCompiler internal constructor(
    private val command: String
) : DefaultTask(), BibliographyCompiler {

    private companion object {
        val CITATION_REGEX = Regex("""^\\citation""")
        val BIB_DATA_REGEX = Regex("""^\\bibdata\{([^}]+)}""")
        val BIB_STYLE_REGEX = Regex("""^\\bibstyle\{([^}]+)}""")
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
        aux.useLines { lines ->
            lines.forEach { line ->
                BIB_DATA_REGEX.find(line)?.let { result ->
                    val file = result.groupValues[1].let { name ->
                        when {
                            name.contains('.') -> name
                            else -> "$name.$BIB"
                        }
                    }
                    sourceDir.resolve(file).takeIf(File::exists)?.copyTo(aux.resolveSibling(file))
                }
                BIB_STYLE_REGEX.find(line)?.let { result ->
                    val file = result.groupValues[1].let { name ->
                        when {
                            name.contains('.') -> name
                            else -> "$name.$BST"
                        }
                    }
                    sourceDir.resolve(file).takeIf(File::exists)?.copyTo(aux.resolveSibling(file))
                }
            }
        }

        project.exec { spec ->
            spec.workingDir = aux.parentFile
            spec.executable = command
            spec.args(aux.nameWithoutExtension)
        }
        didWork = true
    }
}