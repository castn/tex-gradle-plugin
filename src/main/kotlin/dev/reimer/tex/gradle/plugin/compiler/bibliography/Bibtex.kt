package dev.reimer.tex.gradle.plugin.compiler.bibliography

import dev.reimer.tex.gradle.plugin.internal.FileExtensions
import java.io.File

open class Bibtex : DefaultBibliographyCompiler("bibtex", Copier) {

    object Copier : BibliographyCopier {
        private val BIB_DATA_REGEX = Regex("""^\\bibdata\{([^}]+)}""")
        private val BIB_STYLE_REGEX = Regex("""^\\bibstyle\{([^}]+)}""")

        override fun findResources(auxFile: File): Iterable<File> {
            return auxFile.useLines { lines ->
                lines.mapNotNull { line ->
                    val bibData = BIB_DATA_REGEX.find(line)
                    val bibStyle = BIB_STYLE_REGEX.find(line)
                    when {
                        bibData != null -> {
                            val file = bibData.groupValues[1].let { name ->
                                when {
                                    name.contains('.') -> name
                                    else -> "$name.${FileExtensions.BIB}"
                                }
                            }
                            File(file)
                        }
                        bibStyle != null -> {
                            val file = bibStyle.groupValues[1].let { name ->
                                when {
                                    name.contains('.') -> name
                                    else -> "$name.${FileExtensions.BST}"
                                }
                            }
                            File(file)
                        }
                        else -> null
                    }
                }.asIterable()
            }
        }
    }
}
