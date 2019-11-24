package org.danilopianini.gradle.latex.task

import org.danilopianini.gradle.latex.Latex
import org.danilopianini.gradle.latex.LatexArtifact
import org.danilopianini.gradle.latex.configuration.PdflatexTaskConfiguration
import org.gradle.api.tasks.*

open class PdflatexTask : Exec(),
    PdflatexTaskConfiguration {

    @get:Input
    final override val pdflatexCommand = project.objects.property(String::class.java)

    @get:Input
    final override val pdflatexQuiet = project.objects.property(Boolean::class.java)

    @get:Input
    final override val pdflatexArguments = project.objects.listProperty(String::class.java)

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
        pdflatexCommand.set(artifact.pdflatexCommand)
        pdflatexQuiet.set(artifact.pdflatexQuiet)
        pdflatexArguments.set(artifact.pdflatexArguments)
        tex.set(artifact.tex)
        pdf.set(artifact.pdf)
    }

    /**
     * Main task action.
     * Empties auxiliary directory.
     */
    @TaskAction
    override fun exec() {
        Latex.LOG.info("Executing ${pdflatexCommand.get()} for ${tex.get()}")
        executable = pdflatexCommand.get()
        if (pdflatexQuiet.get()) {
            args("-quiet")
        }
        args(pdflatexArguments.get())
        args(tex.get().asFile.absolutePath)

        Latex.LOG.debug("Prepared command $commandLine")
        super.exec()
        super.exec()
    }
}