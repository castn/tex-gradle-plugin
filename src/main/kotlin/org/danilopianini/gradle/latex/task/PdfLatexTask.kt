package org.danilopianini.gradle.latex.task

import org.danilopianini.gradle.latex.Latex
import org.danilopianini.gradle.latex.LatexArtifact
import org.danilopianini.gradle.latex.LatexExtension
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.get
import java.io.File

/**
 * Gradle task to run pdflatex on a TeX file.
 * One such task is created for each Latex artifact.
 *
 * @author csabasulyok
 */
internal open class PdfLatexTask : Exec() {

    private val extension = project.extensions[Latex.EXTENSION_NAME] as LatexExtension

    @get:InputFile
    val tex: Property<File> = project.objects.property(File::class.java)

    @get:Input
    @get:OutputFile
    val pdf: Property<File> = project.objects.property(File::class.java)

    @get:Input
    val quiet: Property<Boolean> = project.objects.property(Boolean::class.java)

    @get:Input
    val extraArgs: ListProperty<String> = project.objects.listProperty(String::class.java)

    init {
        group = Latex.TASK_GROUP
        description = "Uses pdfLaTeX to compile ${tex.get()} into ${pdf.get()}"
    }

    fun fromArtifact(artifact: LatexArtifact) {
        tex.set(artifact.tex.asFile)
        pdf.set(artifact.pdf.asFile)
        quiet.set(artifact.quiet)
        extraArgs.set(artifact.extraArgs)
    }

    /**
     * Main task action.
     * Empties auxiliary directory.
     */
    @TaskAction
    override fun exec() {
        Latex.LOG.info("Executing ${extension.pdfLatexCommand.get()} for ${tex.get()}")
        executable = extension.pdfLatexCommand.get()
        if (quiet.get()) args("-quiet ")
        args(extraArgs.get())
        args(tex.get().absolutePath)

        Latex.LOG.debug("Prepared command $commandLine")
        super.exec()
        super.exec()
    }
}