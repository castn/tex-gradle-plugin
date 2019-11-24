package org.danilopianini.gradle.latex

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

internal abstract class LatexTask : DefaultTask() {
    /**
     * Latex artifact used to run current task.
     */
    @get:Input
    val artifactName: Property<String> = project.objects.property(String::class.java)

    init {
        group = Latex.TASK_GROUP
        description = "Builds the ${artifactName.get()} LaTeX artifact."
    }
}