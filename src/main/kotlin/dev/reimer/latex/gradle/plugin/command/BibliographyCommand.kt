package dev.reimer.latex.gradle.plugin.command

import dev.reimer.latex.gradle.plugin.LatexPlugin
import dev.reimer.latex.gradle.plugin.configuration.LatexTaskConfiguration
import dev.reimer.latex.gradle.plugin.internal.auxFile
import dev.reimer.latex.gradle.plugin.internal.hasBibFile
import org.gradle.api.GradleException
import org.gradle.process.ExecSpec
import java.io.FileNotFoundException

internal class BibliographyCommand(private val command: String) : Command {

    override fun ExecSpec.configure(configuration: LatexTaskConfiguration): Boolean {
        if (!configuration.hasBibFile) {
            LatexPlugin.LOG.info("No bibliography found, skipping $command.")
            return false
        }

        val aux = configuration.auxFile
        if (!aux.exists()) {
            throw GradleException(
                "${aux.absolutePath} does not exist, cannot invoke $command.",
                FileNotFoundException()
            )
        }
        val containsCitations = aux.useLines { lines ->
            lines.any { line ->
                line.contains("""\citation""")
            }
        }
        if (containsCitations) {
            executable = command
            args(aux.path)
            return true
        } else {
            LatexPlugin.LOG.info("No citation in ${aux.absolutePath}, skipping $command.")
            return false
        }
    }
}