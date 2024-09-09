package dev.reimer.tex.gradle.plugin.internal

import dev.reimer.tex.gradle.plugin.contains
import org.gradle.api.Project
import java.io.File
import java.io.FileFilter

internal class TexResourcesFileFilter(private val project: Project) : FileFilter {
    override fun accept(pathname: File) = pathname.isFile && pathname !in project.layout.buildDirectory.asFileTree
}