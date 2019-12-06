package dev.reimer.tex.gradle.plugin.compiler.bibliography

import dev.reimer.tex.gradle.plugin.internal.FileExtensions
import groovy.util.Node
import groovy.util.XmlParser
import groovy.xml.QName
import java.io.File

open class Biber : DefaultBibliographyCompiler("biber", Copier) {

    object Copier : BibliographyCopier {
        private val BIBLATEX_NAMESPACE = "https://sourceforge.net/projects/biblatex"
        private val BIB_DATA_NAME = QName(BIBLATEX_NAMESPACE, "bibdata")
        private val DATASOURCE_NAME = QName(BIBLATEX_NAMESPACE, "datasource")

        override fun findResources(auxFile: File): Iterable<File> {
            return XmlParser()
                .parse(auxFile)
                .getAt(BIB_DATA_NAME)
                .filterIsInstance<Node>()
                .flatMap { bibDataNode ->
                    bibDataNode
                        .getAt(DATASOURCE_NAME)
                        .filterIsInstance<Node>()
                        .map { datasourceNode ->
                            val file = datasourceNode.text().trim().let { name ->
                                when {
                                    name.contains('.') -> name
                                    else -> "$name.${FileExtensions.BIB}"
                                }
                            }
                            File(file)
                        }
                }
        }
    }
}