package org.danilopianini.gradle.latex.task

import org.danilopianini.gradle.latex.Latex
import org.danilopianini.gradle.latex.LatexArtifact
import org.gradle.api.GradleException
import org.gradle.api.tasks.*
import java.nio.file.Files

open class BibtexTask : Exec(), BibtexConfiguration {

    @get:InputFile
    final override val aux = project.objects.fileProperty()

    @get:InputFile
    final override val bib = project.objects.fileProperty()

    @get:Input
    @get:OutputFile
    final override val bbl = project.objects.fileProperty()

    @get:Input
    final override val bibtexCommand = latexExtension.bibTexCommand

    init {
        group = Latex.TASK_GROUP
        description = "Uses BibTex to compile ${aux.get()} into ${bbl.get()}"

        onlyIf {
            bib.orNull?.asFile?.exists() ?: false
        }
    }

    fun fromArtifact(artifact: LatexArtifact) {
        aux.set(artifact.aux)
        bib.set(artifact.bib)
        bbl.set(artifact.bbl)
    }

    /**
     * Execute bibtex command.
     */
    @TaskAction
    override fun exec() {
        val aux = aux.get().asFile
        if (!aux.exists()) {
            throw GradleException("${aux.absolutePath} does not exist, cannot invoke BibTeX")
        }
        if (Files.lines(aux.toPath()).anyMatch { it.contains("""\citation""") }) {
            executable = bibtexCommand.get()
            args(aux.path)
            args
            super.exec()
        } else {
            Latex.LOG.warn("No citation in ${aux.absolutePath}, BibTeX not invoked.")
        }
    }

}