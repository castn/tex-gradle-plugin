package org.danilopianini.gradle.latex.task

import org.danilopianini.gradle.latex.Latex
import org.danilopianini.gradle.latex.LatexArtifact
import org.danilopianini.gradle.latex.configuration.ConvertImagesTaskConfiguration
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction

open class ConvertImagesTask : DefaultTask(),
    ConvertImagesTaskConfiguration {

    @get:Input
    final override val inkscapeCommand = project.objects.property(String::class.java)

    @get:InputFiles
    final override val images = project.objects.fileCollection()

    init {
        group = Latex.TASK_GROUP
        description = "Converts images to a format, that is readable by pdfLaTeX."
    }

    fun fromArtifact(artifact: LatexArtifact) {
        inkscapeCommand.set(artifact.inkscapeCommand)
        images.setFrom(artifact.images)
    }

    @TaskAction
    fun convert() {
        // TODO
    }
}