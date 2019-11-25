package dev.reimer.latex.gradle.plugin

import java.io.File

internal class LatexArtifactTaskNames(artifact: LatexArtifact) {

    companion object {
        private val ILLEGAL_CHARS_GRADLE = setOf('/', '\\', ':', '<', '>', '"', '?', '*', '|')
        private val ILLEGAL_CHARS = ILLEGAL_CHARS_GRADLE + setOf(File.pathSeparatorChar, '-', '.')
    }

    private val suffix = artifact.name
        .removeSuffix(".tex")
        .capitalizeAt(ILLEGAL_CHARS)

    val build = "build$suffix"

    val pdf = "pdf$suffix"

    val pdfPreBibliography = "pdfPreBibliography$suffix"

    val bibliography = "bibliography$suffix"

    val convertImages = "convertImagesForPdf$suffix"

    val all = setOf(
        build,
        pdf,
        pdfPreBibliography,
        bibliography,
        convertImages
    )
}

private fun String.capitalizeAt(delimiter: Char): String {
    return splitToSequence(delimiter)
        .joinToString("", transform = String::capitalize)
}

private fun String.capitalizeAt(delimiters: Iterable<Char>): String {
    return delimiters.fold(this) { acc, delimiter ->
        acc.capitalizeAt(delimiter)
    }
}
