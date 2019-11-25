package dev.reimer.latex.gradle.plugin.task

import dev.reimer.latex.gradle.plugin.Latex
import dev.reimer.latex.gradle.plugin.LatexArtifact
import dev.reimer.latex.gradle.plugin.command.BibliographyCommand
import dev.reimer.latex.gradle.plugin.configuration.BibliographyConfiguration
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.process.internal.ExecAction

open class BibliographyTask : Exec(), BibliographyConfiguration {

    @get:Input
    final override val bibliographyCommand: Property<BibliographyCommand> =
        project.objects.property(BibliographyCommand::class.java)

    @get:Input
    final override val quiet: Property<Boolean> = project.objects.property(Boolean::class.java)

    @get:InputFile
    final override val aux: RegularFileProperty = project.objects.fileProperty()

    @get:InputFile
    final override val bib: RegularFileProperty = project.objects.fileProperty()

    @get:Input
    @get:OutputFile
    final override val bbl: RegularFileProperty = project.objects.fileProperty()

    override fun getGroup() = Latex.TASK_GROUP
    override fun getDescription() = "Uses BibTex to compile ${aux.get()} into ${bbl.get()}"

    init {
        onlyIf {
            bib.orNull?.asFile?.exists() ?: false
        }
    }

    fun fromArtifact(artifact: LatexArtifact) {
        bibliographyCommand.set(artifact.bibliographyCommand)
        quiet.set(artifact.quiet)
        aux.set(artifact.aux)
        bib.set(artifact.bib)
        bbl.set(artifact.bbl)
    }

    @TaskAction
    override fun exec() {
        val action: ExecAction = execActionFactory.newExecAction()
        bibliographyCommand.get().execute(action, this)
    }
}