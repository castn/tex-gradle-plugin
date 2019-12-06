package dev.reimer.tex.gradle.plugin

import dev.reimer.tex.gradle.plugin.task.TexCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.language.base.plugins.LifecycleBasePlugin

class TexPlugin : Plugin<Project> {

    companion object {
        const val TASK_GROUP = "tex"
        const val EXTENSION_NAME = "tex"
        @Suppress("UnstableApiUsage")
        const val ASSEMBLE_TASK_NAME = LifecycleBasePlugin.ASSEMBLE_TASK_NAME
        const val ASSEMBLE_TASK_PREFIX = "assemble"
        const val ASSEMBLE_TASK_SUFFIX = "tex"
    }

    override fun apply(project: Project) {
        // Register project extension.
        project.extensions.create(
            TexScope::class.java,
            EXTENSION_NAME,
            TexExtension::class.java,
            project
        )

        val assemble = project.tasks.named(ASSEMBLE_TASK_NAME)

        // Register standard task for compiling TeX source set.
        val assembleTex = project.tasks.register(
            ASSEMBLE_TASK_PREFIX + ASSEMBLE_TASK_SUFFIX.capitalize()
        ) { task ->
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
