package org.danilopianini.gradle.latex

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.provider.Property
import java.util.concurrent.TimeUnit

/**
 * Gradle extension to create new dynamic tasks & maintain and manage latex artifacts.
 * Registered to Gradle as extension in LatexPlugin. Thereafter the instance can be accessed via project.latex
 *
 */
open class LatexExtension @JvmOverloads constructor(
    private val project: Project,
    // val auxDir: Property<File> = project.propertyWithDefault(project.file(".gradle/latex-temp")),
    /**
     * Utilities for easy execution.
     * After adding extension, can be accessed via project.latex.utils
     */
    val quiet: Property<Boolean> = project.propertyWithDefault { true },
    val terminalEmulator: Property<String> = project.propertyWithDefault { "bash" },
    val waitTime: Property<Long> = project.propertyWithDefault { 1L },
    val waitUnit: Property<TimeUnit> = project.propertyWithDefault { TimeUnit.MINUTES },
    val pdfLatexCommand: Property<String> = project.propertyWithDefault { "pdflatex" },
    val bibTexCommand: Property<String> = project.propertyWithDefault { "bibtex" },
    val inkscapeCommand: Property<String> = project.propertyWithDefault { "inkscape" },
    val gitLatexdiffCommand: Property<String> = project.propertyWithDefault { "git latexdiff" }
) : NamedDomainObjectContainer<LatexArtifact> by LatexArtifactContainer(project) {

    private val runAll = project.tasks.register("buildLatex") { task ->
        task.group = Latex.TASK_GROUP
        task.description = "Run all LaTeX tasks"
    }

    init {
        configureEach { artifact ->

            val pdfLatexPreBibtex =
                project.tasks.register("pdfLatexPreBibtex${artifact.taskSuffix}", PdfLatexTask::class.java) { task ->
                    task.artifact = artifact
                    task.dependsOn(task.artifact.dependsOn.get().map { project.task("buildLatex${it.taskSuffix}") })
                }

            val bibTexTask = project.tasks.register("bibtex${artifact.taskSuffix}", BibtexTask::class.java) { task ->
                task.artifact = artifact

                // Skip executing this task, if no bibliography has been specified.
                task.onlyIf {
                    task.artifact.bib.isPresent
                }

                task.dependsOn(pdfLatexPreBibtex)
            }

            val pdfLatex =
                project.tasks.register("pdfLatexAfterBibtex${artifact.taskSuffix}", PdfLatexTask::class.java) { task ->
                    task.artifact = artifact
                    if (artifact.hasBib) {
                        task.dependsOn(bibTexTask)
                    } else {
                        task.dependsOn(task.artifact.dependsOn.get().map { project.task("buildLatex${it.taskSuffix}") })
                    }
                }

            val run = project.tasks.register("buildLatex${artifact.taskSuffix}", LatexTask::class.java) { task ->
                task.artifact = artifact
                task.dependsOn(pdfLatex)
            }

            runAll.get().dependsOn(run)
        }
    }

    @JvmOverloads
    @Deprecated(message = "Create LatexArtifact directly using create().")
    @Suppress("DEPRECATION")
    operator fun String.invoke(configuration: LatexArtifactBuilder.() -> Unit = { }): LatexArtifact {
        return create(this) { artifact ->
            LatexArtifactBuilder(project, this)
                .apply(configuration)
                .applyTo(artifact)
        }
    }
}
