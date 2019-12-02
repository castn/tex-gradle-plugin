package dev.reimer.tex.gradle.plugin.compiler.tex

import dev.reimer.tex.gradle.plugin.directoryProperty
import dev.reimer.tex.gradle.plugin.fileProperty
import dev.reimer.tex.gradle.plugin.internal.FileExtensions.PDF
import dev.reimer.tex.gradle.plugin.property
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskAction

open class Pdflatex : DefaultTask(), TexCompiler {

    final override val tex = project.fileProperty()

    final override val quiet = project.property<Boolean>()

    final override val overwrite = project.property<Boolean>() // TODO Use this property.

    final override val destinationDir = project.directoryProperty()

    final override val buildDir = project.directoryProperty()

    final override val destination: Provider<RegularFile> =
        destinationDir.map { dir ->
            dir.file("${tex.get().asFile.nameWithoutExtension}.$PDF")
        }

    @TaskAction
    final override fun compileTex() {
        val tex = tex.get().asFile
        if (!tex.exists()) {
            didWork = false
            return
        }
        project.exec { spec ->
            spec.workingDir = tex.parentFile
            spec.executable = "pdflatex"
            if (quiet.get()) {
                spec.args("-quiet")
            }
            // Halt immediately on errors.
            spec.args("-halt-on-error", "-interaction=nonstopmode")
            // Save output files to
            spec.args("-output-directory=${destinationDir.get().asFile.absolutePath}")
            spec.args("-aux-directory=${buildDir.get().asFile.absolutePath}")
            spec.args(tex.absolutePath)
        }
    }
}