package org.danilopianini.gradle.latex.task

import org.danilopianini.gradle.latex.Latex
import org.danilopianini.gradle.latex.LatexArtifact
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction

open class ConvertImagesTask : DefaultTask(), ConvertImagesConfiguration {

    @get:InputFiles
    final override val images = project.objects.fileCollection()

    @get:Input
    final override val inkscapeCommand = latexExtension.inkscapeCommand

    init {
        group = Latex.TASK_GROUP
        description = "Converts images to a format, that is readable by pdfLaTeX."
    }

    fun fromArtifact(artifact: LatexArtifact) {
        images.from(artifact.imageFiles)
    }

    @TaskAction
    fun convert() {
        // TODO
    }
}