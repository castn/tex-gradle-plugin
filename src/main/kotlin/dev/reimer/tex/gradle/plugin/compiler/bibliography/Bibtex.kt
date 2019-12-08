package dev.reimer.tex.gradle.plugin.compiler.bibliography

import dev.reimer.tex.gradle.plugin.internal.FileExtensions
import org.gradle.api.provider.Provider
import java.io.File

open class Bibtex : DefaultBibliographyCompiler() {

    private companion object {
        const val BIBTEX_COMMAND = "bibtex"
        val CITATION_REGEX = Regex("""^\\citation\{([^}]+)}""")
        val BIB_DATA_REGEX = Regex("""^\\bibdata\{([^}]+)}""")
        val BIB_STYLE_REGEX = Regex("""^\\bibstyle\{([^}]+)}""")
        val INPUT_REGEX = Regex("""^\\@input\{([^}]+)}""")

        fun parseResources(buildDir: File, auxFile: File): Iterable<File> {
            if (!auxFile.exists()) return emptyList()
            return auxFile.useLines { lines ->
                lines.asIterable().flatMap { line ->
                    val bibData = BIB_DATA_REGEX.find(line)
                    val bibStyle = BIB_STYLE_REGEX.find(line)
                    val input = INPUT_REGEX.find(line)
                    when {
                        bibData != null -> {
                            val file = bibData.groupValues[1].let { name ->
                                when {
                                    name.contains('.') -> name
                                    else -> "$name.${FileExtensions.BIB}"
                                }
                            }
                            listOf(File(file))
                        }
                        bibStyle != null -> {
                            val file = bibStyle.groupValues[1].let { name ->
                                when {
                                    name.contains('.') -> name
                                    else -> "$name.${FileExtensions.BST}"
                                }
                            }
                            listOf(File(file))
                        }
                        input != null -> {
                            val file = input.groupValues[1]
                            parseResources(buildDir, buildDir.resolve(file))
                        }
                        else -> emptyList()
                    }
                }.toList()
            }
        }

        fun parseCitations(auxFile: File): Boolean {
            if (!auxFile.exists()) return false
            return auxFile.useLines { lines ->
                lines.any { line ->
                    CITATION_REGEX.containsMatchIn(line)
                }
            }
        }
    }

    final override val command = BIBTEX_COMMAND

    final override val resources: Provider<Iterable<File>> =
        auxFile.map { parseResources(it.asFile, buildDir.get().asFile) }

    final override val containsCitations: Provider<Boolean> = auxFile.map { parseCitations(it.asFile) }
}
