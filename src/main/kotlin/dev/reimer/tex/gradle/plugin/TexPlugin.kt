package dev.reimer.tex.gradle.plugin

import dev.reimer.tex.gradle.plugin.task.TexCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.language.base.plugins.LifecycleBasePlugin

class TexPlugin : Plugin<Project> {

    companion object {
        const val TASK_GROUP = "tex"
        const val EXTENSION_NAME = "tex"
        @Suppress("UnstableApiUsage")
        const val ASSEMBLE_TASK_NAME = LifecycleBasePlugin.ASSEMBLE_TASK_NAME
        const val ASSEMBLE_TEX_TASK_NAME = ASSEMBLE_TASK_NAME + "Tex"
    }

    override fun apply(project: Project) {
        @Suppress("UnstableApiUsage")
        project.plugins.apply(LifecycleBasePlugin::class)

        // Register project extension.
        project.extensions.create(
            TexScope::class.java,
            EXTENSION_NAME,
            TexExtension::class.java,
            project
        )

        val assemble: TaskProvider<Task> = project.tasks.named(ASSEMBLE_TASK_NAME)

        // Register standard task for compiling TeX source set.
        val assembleTex: TaskProvider<Task> = project.tasks.register(ASSEMBLE_TEX_TASK_NAME) { task ->
            // Make TeX build a dependency of the global build task.
            assemble.get().dependsOn(task)
        }

        // Add all other TexCompile tasks as dependencies to the main task.
        project.tasks.whenObjectAdded { task ->
            if (task is TexCompile) {
                assembleTex.get().dependsOn(task)
            }
        }
    }
}
