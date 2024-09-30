package dev.reimer.tex.gradle.plugin.internal

import org.gradle.api.Project
import java.io.File
import java.io.FileFilter

internal class TexCompileFileFilter(private val project: Project) : FileFilter {
    override fun accept(pathname: File) =
        pathname.extension == FileExtensions.TEX && pathname.isFile && pathname !in project.layout.buildDirectory.asFileTree
}