package org.danilopianini.gradle.latex

import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.get
import java.nio.file.Files

internal open class BibtexTask : LatexTask() {

    private val extension = project.extensions[Latex.EXTENSION_NAME] as LatexExtension

    init {
        onlyIf {
            val bib = artifact.bib
            bib.orNull?.exists() ?: false
        }

        description = "Uses BibTex to compile ${artifact.aux.get()} into ${artifact.name}.bbl"
    }

    @get:InputFiles
    override val inputFiles: FileCollection
        get() = project.files(
            *(listOfNotNull(artifact.aux.get(), artifact.tex.get(), artifact.bib.get()).toTypedArray())
        )

    /**
     * Execute bibtex command for given latex artifact.
     *
     * @property artifact Any Latex artifact with the tex and bib properties set.
     */
    @TaskAction
    fun bibTex() {
        val aux = artifact.aux.get()
        if (!aux.exists()) {
            throw GradleException("${aux.absolutePath} does not exist, cannot invoke ${extension.bibTexCommand.get()}")
        }
        if (Files.lines(aux.toPath()).anyMatch { it.contains("""\citation""") }) {
            "${extension.bibTexCommand.get()} ${aux.name}".runScript()
        } else {
            Latex.LOG.warn("No citation in ${aux.absolutePath}, bibtex not invoked.")
        }
    }

}