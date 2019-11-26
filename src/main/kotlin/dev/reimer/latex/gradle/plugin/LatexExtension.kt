package dev.reimer.latex.gradle.plugin

import dev.reimer.latex.gradle.plugin.command.BibtexCommand
import dev.reimer.latex.gradle.plugin.command.Command
import dev.reimer.latex.gradle.plugin.command.NopCommand
import dev.reimer.latex.gradle.plugin.command.PdflatexCommand
import dev.reimer.latex.gradle.plugin.configuration.LatexExtensionConfiguration
import dev.reimer.latex.gradle.plugin.task.LatexTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

/**
 * Gradle extension to create new dynamic tasks & maintain and manage latex artifacts.
 * Registered to Gradle as extension in LatexPlugin. Thereafter the instance can be accessed via project.latex
 *
 */
open class LatexExtension(
    private val project: Project
    // val auxDir: Property<File> = project.propertyWithDefault(project.file(".gradle/latex-temp")),
) : LatexExtensionConfiguration {

    override val pdfCommand: Property<Command> = project.propertyWithDefault { PdflatexCommand }

    override val bibliographyCommand: Property<Command> = project.propertyWithDefault { BibtexCommand }

    override val convertImagesCommand: Property<Command> = project.propertyWithDefault { NopCommand }

    override val quiet: Property<Boolean> = project.propertyWithDefault { true }

    override val overwrite: Property<Boolean> = project.propertyWithDefault { true }

    override val auxDirectory: DirectoryProperty = project.objects.directoryProperty()

    private val buildAllTask = project.tasks.register("latex") { task ->
        task.group = LatexPlugin.TASK_GROUP
        task.description = "Run all LaTeX tasks"
    }

    init {
        project.tasks.whenObjectAdded(::registerTaskIfNeeded)
    }

    private fun registerTaskIfNeeded(task: Task) {
        if (task is LatexTask) {
            buildAllTask.get().dependsOn(task)
        }
    }
}
