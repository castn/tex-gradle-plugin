package dev.reimer.tex.gradle.plugin.compiler.bibliography

import dev.reimer.tex.gradle.plugin.internal.FileExtensions
import groovy.util.Node
import groovy.util.XmlParser
import groovy.xml.QName
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputFile
import java.io.File

open class Biber : DefaultBibliographyCompiler() {

    private companion object {
        private const val BIBER_COMMAND = "biber"
        private const val BIBLATEX_NAMESPACE = "https://sourceforge.net/projects/biblatex"
        private val BIB_DATA_TAG = QName(BIBLATEX_NAMESPACE, "bibdata")
        private val DATA_SOURCE_TAG = QName(BIBLATEX_NAMESPACE, "datasource")
        private val SECTION_TAG = QName(BIBLATEX_NAMESPACE, "section")
        private val CITE_KEY_TAG = QName(BIBLATEX_NAMESPACE, "citekey")

        private fun parseResources(bcfFile: File): Iterable<File> {
            if (!bcfFile.isFile || !bcfFile.exists()) {
                // Assume that no bibliography has been included in the TeX source.
                return emptyList()
            }
            return XmlParser()
                .parse(bcfFile)
                .getAt(BIB_DATA_TAG)
                .filterIsInstance<Node>()
                .flatMap { bibDataNode ->
                    bibDataNode
                        .getAt(DATA_SOURCE_TAG)
                        .filterIsInstance<Node>()
                        .map { dataSourceNode ->
                            val file = dataSourceNode.text().trim().let { name ->
                                when {
                                    name.contains('.') -> name
                                    else -> "$name.${FileExtensions.BIB}"
                                }
                            }
                            File(file)
                        }
                }
        }

        private fun parseCitations(bcfFile: File): Boolean {
            if (!bcfFile.isFile || !bcfFile.exists()) return false
            return XmlParser()
                .parse(bcfFile)
                .getAt(SECTION_TAG)
                .filterIsInstance<Node>()
                .any { sectionNode ->
                    sectionNode
                        .getAt(CITE_KEY_TAG)
                        .filterIsInstance<Node>()
                        .any { citeKeyNode ->
                            citeKeyNode.text().isNotBlank()
                        }
                }
        }
    }

    final override val command = BIBER_COMMAND

    private val bcfFileName: Provider<String> = jobName.map { "$it.${FileExtensions.BCF}" }

    @get:InputFile
    protected val bcfFile: Provider<RegularFile> =
        project.layout.file(buildDir.map { it.asFile.resolve(bcfFileName.get()) })

    final override val resources: Provider<Iterable<File>> =
        bcfFile.map { parseResources(it.asFile) }

    final override val containsCitations: Provider<Boolean> =
        bcfFile.map { parseCitations(it.asFile) }

}