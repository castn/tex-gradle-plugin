package dev.reimer.latex.gradle.plugin

import dev.reimer.latex.gradle.plugin.configuration.LatexExtensionConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * A Plugin configuring the project for publishing on Maven Central
 */
class LatexPlugin : Plugin<Project> {
    companion object {
        /**
         * The name of the publication to be created.
         */
        const val TASK_GROUP = "LaTeX"
        const val EXTENSION_NAME = "latex"
        val LOG: Logger = LoggerFactory.getLogger(LatexPlugin::class.java)
    }

    override fun apply(project: Project) {
        project.extensions.create(
            LatexExtensionConfiguration::class.java,
            EXTENSION_NAME,
            LatexExtension::class.java,
            project
        )
    }
}
