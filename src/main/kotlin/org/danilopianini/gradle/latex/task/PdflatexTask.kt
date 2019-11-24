package org.danilopianini.gradle.latex.task

import org.danilopianini.gradle.latex.Latex
import org.danilopianini.gradle.latex.LatexArtifact
import org.danilopianini.gradle.latex.LatexExtension
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.get

open class PdflatexTask : Exec(), PdflatexConfiguration {

    private val extension = project.extensions[Latex.EXTENSION_NAME] as LatexExtension

    @get:InputFile
    final override val tex = project.objects.fileProperty()

    @get:Input
    @get:OutputFile
    final override val pdf = project.objects.fileProperty()

    @get:Input
    final override val quiet = project.objects.property(Boolean::class.java)

    @get:Input
    final override val extraArgs = project.objects.listProperty(String::class.java)

    @get:Input
    final override val pdflatexCommand = latexExtension.pdfLatexCommand

    init {
        group = Latex.TASK_GROUP
        description = "Uses pdfLaTeX to compile ${tex.get()} into ${pdf.get()}"
    }

    fun fromArtifact(artifact: LatexArtifact) {
        tex.set(artifact.tex)
        pdf.set(artifact.pdf)
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
        if (quiet.get()) {
            args("-quiet")
        }
        args(extraArgs.get())
        args(tex.get().asFile.absolutePath)

        Latex.LOG.debug("Prepared command $commandLine")
        super.exec()
        super.exec()
    }
}