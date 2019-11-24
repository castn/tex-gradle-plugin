package org.danilopianini.gradle.latex

import org.danilopianini.gradle.latex.configuration.BibtexCommandConfiguration
import org.danilopianini.gradle.latex.configuration.ConvertImagesCommandConfiguration
import org.danilopianini.gradle.latex.configuration.PdflatexCommandConfiguration
import org.danilopianini.gradle.latex.task.BibtexTask
import org.danilopianini.gradle.latex.task.ConvertImagesTask
import org.danilopianini.gradle.latex.task.PdflatexTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider

/**
 * Gradle extension to create new dynamic tasks & maintain and manage latex artifacts.
 * Registered to Gradle as extension in LatexPlugin. Thereafter the instance can be accessed via project.latex
 *
 */
open class LatexExtension @JvmOverloads constructor(
    private val project: Project
    // val auxDir: Property<File> = project.propertyWithDefault(project.file(".gradle/latex-temp")),
) : NamedDomainObjectContainer<LatexArtifact> by LatexArtifactContainer(project),
    PdflatexCommandConfiguration, BibtexCommandConfiguration, ConvertImagesCommandConfiguration {

    override val pdflatexCommand: Property<String> = project.propertyWithDefault { "pdflatex" }

    override val pdflatexQuiet: Property<Boolean> = project.propertyWithDefault { true }

    override val pdflatexArguments = project.listPropertyWithDefault {
        listOf("-shell-escape", "-synctex=1", "-interaction=nonstopmode", "-halt-on-error")
    }

    override val bibtexCommand: Property<String> = project.propertyWithDefault { "bibtex" }

    override val inkscapeCommand: Property<String> = project.propertyWithDefault { "inkscape" }

    private val runAll = project.tasks.register("buildLatex") { task ->
        task.group = Latex.TASK_GROUP
        task.description = "Run all LaTeX tasks"
    }

    init {
        configureEach { artifact ->

            val convertImages: TaskProvider<ConvertImagesTask> =
                project.tasks.register(
                    "convertImagesForPdflatex${artifact.taskSuffix}",
                    ConvertImagesTask::class.java
                ) { task ->
                    task.fromArtifact(artifact)
                }

            val pdflatexPreBibtex: TaskProvider<PdflatexTask> =
                project.tasks.register("pdflatexPreBibtex${artifact.taskSuffix}", PdflatexTask::class.java) { task ->
                    task.fromArtifact(artifact)
                    task.dependsOn(convertImages)
                }

            val bibTexTask: TaskProvider<BibtexTask> =
                project.tasks.register("bibtex${artifact.taskSuffix}", BibtexTask::class.java) { task ->
                    task.fromArtifact(artifact)

                // Skip executing this task, if no bibliography has been specified.
                task.onlyIf {
                    artifact.bib.isPresent
                }

                    task.dependsOn(pdflatexPreBibtex)
            }

            val pdflatex: TaskProvider<PdflatexTask> =
                project.tasks.register("pdflatexAfterBibtex${artifact.taskSuffix}", PdflatexTask::class.java) { task ->
                    task.fromArtifact(artifact)
                    task.dependsOn(convertImages)
                    if (artifact.hasBib) {
                        task.dependsOn(bibTexTask)
                    }
                }

            val run: TaskProvider<Task> = project.tasks.register("buildLatex${artifact.taskSuffix}") { task ->
                task.group = Latex.TASK_GROUP
                task.description = "Builds the ${artifact.name} LaTeX artifact."

                task.dependsOn(pdflatex)
            }

            // All tasks of this artifact should depend on the artifact's dependencies' tasks.
            val tasks: Set<TaskProvider<out Task>> = setOf(convertImages, pdflatexPreBibtex, bibTexTask, pdflatex, run)
            val dependsOnTasks = artifact.dependsOn.get().map { project.tasks.named("buildLatex${it.taskSuffix}") }
            tasks.forEach { provider ->
                provider.configure { task ->
                    task.dependsOn(dependsOnTasks)
                }
            }

            runAll.get().dependsOn(run)
        }
    }
}
