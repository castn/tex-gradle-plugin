package dev.reimer.tex.gradle.plugin.compiler.bibliography

import java.io.File

internal interface BibliographyCopier {

    /**
     * Find files that need to be copied from the source dir to the build dir,
     * for the bibliography compiler to work.
     */
    fun findResources(auxFile: File): Iterable<File>
}