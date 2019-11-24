package org.danilopianini.gradle.latex.task

import org.danilopianini.gradle.latex.Latex
import org.danilopianini.gradle.latex.LatexArtifact
import org.danilopianini.gradle.latex.configuration.PdfTaskConfiguration
import org.gradle.api.tasks.*

open class PdfTask : Exec(),
    PdfTaskConfiguration {

    @get:Input
    final override val pdfCommand = project.objects.property(String::class.java)

    @get:Input
    final override val pdfQuiet = project.objects.property(Boolean::class.java)

    @get:Input
    final override val pdfArguments = project.objects.listProperty(String::class.java)

    @get:InputFile
    final override val tex = project.objects.fileProperty()

    @get:Input
    @get:OutputFile
    final override val pdf = project.objects.fileProperty()

    init {
        group = Latex.TASK_GROUP
        description = "Uses pdfLaTeX to compile ${tex.get()} into ${pdf.get()}"
    }

    fun fromArtifact(artifact: LatexArtifact) {
        pdfCommand.set(artifact.pdfCommand)
        pdfQuiet.set(artifact.pdfQuiet)
        pdfArguments.set(artifact.pdfArguments)
        tex.set(artifact.tex)
        pdf.set(artifact.pdf)
    }

    /**
     * Main task action.
     * Empties auxiliary directory.
     */
    @TaskAction
    override fun exec() {
        Latex.LOG.info("Executing ${pdfCommand.get()} for ${tex.get()}")
        executable = pdfCommand.get()
        if (pdfQuiet.get()) {
            args("-quiet")
        }
        args(pdfArguments.get())
        args(tex.get().asFile.absolutePath)

        Latex.LOG.debug("Prepared command $commandLine")
        super.exec()
        super.exec()
    }
}