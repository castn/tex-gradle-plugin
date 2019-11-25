package dev.reimer.latex.gradle.plugin.task

import dev.reimer.latex.gradle.plugin.Latex
import dev.reimer.latex.gradle.plugin.LatexArtifact
import dev.reimer.latex.gradle.plugin.command.ConvertImagesCommand
import dev.reimer.latex.gradle.plugin.configuration.ConvertImagesConfiguration
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

    override fun getGroup() = Latex.TASK_GROUP
    override fun getDescription() = "Converts images to a format, that is readable by pdfLaTeX."

    fun fromArtifact(artifact: LatexArtifact) {
        convertImagesCommand.set(artifact.convertImagesCommand)
        quiet.set(artifact.quiet)
        images.setFrom(artifact.images)
    }

    @TaskAction
    override fun exec() {
        val action: ExecAction = execActionFactory.newExecAction()
        convertImagesCommand.get().execute(action, this)
    }
}