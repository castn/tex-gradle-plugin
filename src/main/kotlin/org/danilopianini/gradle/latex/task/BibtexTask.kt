package org.danilopianini.gradle.latex.task

import org.danilopianini.gradle.latex.Latex
import org.danilopianini.gradle.latex.LatexArtifact
import org.danilopianini.gradle.latex.configuration.BibtexTaskConfiguration
import org.gradle.api.GradleException
import org.gradle.api.tasks.*

open class BibtexTask : Exec(), BibtexTaskConfiguration {

    @get:Input
    final override val bibtexCommand = project.objects.property(String::class.java)

    @get:InputFile
    final override val aux = project.objects.fileProperty()

    @get:InputFile
    final override val bib = project.objects.fileProperty()

    @get:Input
    @get:OutputFile
    final override val bbl = project.objects.fileProperty()

    init {
        group = Latex.TASK_GROUP
        description = "Uses BibTex to compile ${aux.get()} into ${bbl.get()}"

        onlyIf {
            bib.orNull?.asFile?.exists() ?: false
        }
    }

    fun fromArtifact(artifact: LatexArtifact) {
        bibtexCommand.set(artifact.bibtexCommand)
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
            throw GradleException("${aux.absolutePath} does not exist, cannot invoke ${bibtexCommand.get()}.")
        }
        val containsCitations = aux.useLines { lines ->
            lines.any { line ->
                line.contains("""\citation""")
            }
        }
        if (containsCitations) {
            executable = bibtexCommand.get()
            args(aux.path)
            args
            super.exec()
        } else {
            Latex.LOG.warn("No citation in ${aux.absolutePath}, BibTeX not invoked.")
        }
    }
}