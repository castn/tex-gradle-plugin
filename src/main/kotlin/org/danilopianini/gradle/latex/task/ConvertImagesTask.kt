package org.danilopianini.gradle.latex.task

import org.danilopianini.gradle.latex.Latex
import org.danilopianini.gradle.latex.LatexArtifact
import org.danilopianini.gradle.latex.command.ConvertImagesCommand
import org.danilopianini.gradle.latex.configuration.ConvertImagesConfiguration
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.process.internal.ExecAction

open class ConvertImagesTask : Exec(), ConvertImagesConfiguration {

    @get:Input
    final override val convertImagesCommand: Property<ConvertImagesCommand> =
        project.objects.property(ConvertImagesCommand::class.java)

    @get:Input
    final override val quiet: Property<Boolean> = project.objects.property(Boolean::class.java)

    @get:InputFiles
    final override val images: ConfigurableFileCollection = project.objects.fileCollection()

    init {
        group = Latex.TASK_GROUP
        description = "Converts images to a format, that is readable by pdfLaTeX."
    }

    fun fromArtifact(artifact: LatexArtifact) {
        convertImagesCommand.set(artifact.convertImagesCommand)
        quiet.set(artifact.quiet)
        images.setFrom(artifact.images)
    }

    @TaskAction
    fun convert() {
        val action: ExecAction = execActionFactory.newExecAction()
        convertImagesCommand.get().execute(action, this)
    }
}