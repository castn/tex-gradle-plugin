package dev.reimer.latex.gradle.plugin.command

import dev.reimer.latex.gradle.plugin.configuration.LatexTaskConfiguration
import dev.reimer.latex.gradle.plugin.withExtension
import org.gradle.process.ExecSpec
import java.io.File

object InkscapeCommand : Command {

    override fun ExecSpec.configure(configuration: LatexTaskConfiguration): Boolean {
        executable = "inkscape"
        args("--without-gui")
        args("--export-type=\"pdf\"")
        val images = configuration.images
            .filter { it.isSupported() }
            .filter { it.needsConversion() }
            .map { it.absolutePath }
        images.forEach { args(it) }
        return images.isNotEmpty()
    }

    private fun File.needsConversion(): Boolean {
        val pdf = withExtension("pdf") // TODO What about .PDF or .Pdf?
            .takeIf(File::exists)
            ?: return true
        return pdf.lastModified() >= lastModified()
    }

    private val SUPPORTED_EXTENSIONS = listOf("svg", "emf")
    private fun File.isSupported() = extension.toLowerCase() in SUPPORTED_EXTENSIONS
}