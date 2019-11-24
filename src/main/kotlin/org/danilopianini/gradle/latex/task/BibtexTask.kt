package org.danilopianini.gradle.latex.task

import org.danilopianini.gradle.latex.Latex
import org.danilopianini.gradle.latex.LatexArtifact
import org.danilopianini.gradle.latex.LatexExtension
import org.gradle.api.GradleException
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.get
import java.io.File
import java.nio.file.Files

internal open class BibtexTask : Exec() {

    private val extension = project.extensions[Latex.EXTENSION_NAME] as LatexExtension

    @get:InputFile
    val aux: Property<File> = project.objects.property(File::class.java)

    @get:InputFile
    val bib: Property<File> = project.objects.property(File::class.java)

    @get:Input
    @get:OutputFile
    val bbl: Property<File> = project.objects.property(File::class.java)

    init {
        group = Latex.TASK_GROUP
        description = "Uses BibTex to compile ${aux.get()} into ${bbl.get()}"

        onlyIf {
            bib.orNull?.exists() ?: false
        }
    }

    fun fromArtifact(artifact: LatexArtifact) {
        aux.set(artifact.aux.asFile)
        bib.set(artifact.bib.asFile)
        bbl.set(artifact.bbl.asFile)
    }

    /**
     * Execute bibtex command.
     */
    @TaskAction
    override fun exec() {
        val aux = aux.get()
        if (!aux.exists()) {
            throw GradleException("${aux.absolutePath} does not exist, cannot invoke ${extension.bibTexCommand.get()}")
        }
        if (Files.lines(aux.toPath()).anyMatch { it.contains("""\citation""") }) {
            commandLine(extension.bibTexCommand.get(), aux.path)
            super.exec()
        } else {
            Latex.LOG.warn("No citation in ${aux.absolutePath}, bibtex not invoked.")
        }
    }

}