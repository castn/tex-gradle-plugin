package dev.reimer.latex.gradle.plugin

import dev.reimer.latex.gradle.plugin.command.*
import dev.reimer.latex.gradle.plugin.configuration.ExtensionConfiguration
import dev.reimer.latex.gradle.plugin.task.BibliographyTask
import dev.reimer.latex.gradle.plugin.task.ConvertImagesTask
import dev.reimer.latex.gradle.plugin.task.PdfTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.get

/**
 * Gradle extension to create new dynamic tasks & maintain and manage latex artifacts.
 * Registered to Gradle as extension in LatexPlugin. Thereafter the instance can be accessed via project.latex
 *
 */
open class LatexExtension(
    private val project: Project
    // val auxDir: Property<File> = project.propertyWithDefault(project.file(".gradle/latex-temp")),
) : NamedDomainObjectContainer<LatexArtifact> by LatexArtifactContainer(
    project
),
    ExtensionConfiguration {

    override val pdfCommand: Property<PdfCommand> = project.propertyWithDefault { PdflatexCommand }

    override val bibliographyCommand: Property<BibliographyCommand> = project.propertyWithDefault { BibtexCommand }

    override val convertImagesCommand: Property<ConvertImagesCommand> =
        project.propertyWithDefault { NopConvertImagesCommand }

    override val quiet: Property<Boolean> = project.propertyWithDefault { true }

    private val buildAllTask = project.tasks.register("latex") { task ->
        task.group = Latex.TASK_GROUP
        task.description = "Run all LaTeX tasks"
    }

    init {
        whenObjectAdded(::registerArtifactTasks)
        whenObjectRemoved(::unregisterArtifactTasks)
    }

    private fun registerArtifactTasks(artifact: LatexArtifact) {
        val taskNames = artifact.taskNames

        val convertImagesTask: TaskProvider<ConvertImagesTask> =
            project.tasks.register(taskNames.convertImages, ConvertImagesTask::class.java) { task ->
                task.fromArtifact(artifact)
            }

        val pdfPreBibliographyTask: TaskProvider<PdfTask> =
            project.tasks.register(taskNames.pdfPreBibliography, PdfTask::class.java) { task ->
                task.fromArtifact(artifact)
                task.dependsOn(convertImagesTask)
            }

        val bibliographyTask: TaskProvider<BibliographyTask> =
            project.tasks.register(taskNames.bibliography, BibliographyTask::class.java) { task ->
                task.fromArtifact(artifact)

                // Skip executing this task, if no bibliography has been specified.
                task.onlyIf {
                    artifact.bib.isPresent
                }

                task.dependsOn(pdfPreBibliographyTask)
            }

        val pdfTask: TaskProvider<PdfTask> =
            project.tasks.register(taskNames.pdf, PdfTask::class.java) { task ->
                task.fromArtifact(artifact)
                task.dependsOn(convertImagesTask)
                if (artifact.hasBib) {
                    task.dependsOn(bibliographyTask)
                }
            }

        val buildTask: TaskProvider<Task> = project.tasks.register(taskNames.build) { task ->
            task.group = Latex.TASK_GROUP
            task.description = "Builds the ${artifact.name} LaTeX artifact."

            task.dependsOn(pdfTask)
        }

        // All tasks of this artifact should depend on the artifact's dependencies' tasks.
        addTaskDependencies(taskNames.all, artifact.dependsOn.get().map { it.taskNames.build })

        buildAllTask.get().dependsOn(buildTask)
    }

    private fun unregisterArtifactTasks(artifact: LatexArtifact) {
        artifact.taskNames.all.forEach { name ->
            val task = project.tasks[name]
            project.tasks.remove(task)
        }
    }

    private fun addTaskDependencies(taskNames: Iterable<String>, dependencyTaskNames: Iterable<String>) {
        val dependencyTasks = dependencyTaskNames.map { name ->
            project.tasks.named(name)
        }
        taskNames.forEach { name ->
            project.tasks.named(name) { task ->
                task.dependsOn(dependencyTasks)
            }
        }
    }
}
