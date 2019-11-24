package org.danilopianini.gradle.latex

import org.gradle.api.Project
import java.io.File

@Deprecated(message = "Create LatexArtifact directly using LatexExtension.create().")
data class LatexArtifactBuilder(private val project: Project, val name: String) {

    private var bibChanged = false
    var bib: String? = project.file(fromName("bib")).takeIf(File::exists)?.absolutePath
        set(value) {
            field = value
            bibChanged = true
        }

    private var dependsOnChanged = false
    var dependsOn: Iterable<LatexArtifact> = emptyList()
        set(value) {
            field = value
            dependsOnChanged = true
        }

    private var imagesChanged = false
    var images: Iterable<File> = emptyList()
        set(value) {
            field = value
            imagesChanged = true
        }

    private var extraArgumentsChanged = false
    var extraArguments: Iterable<String> = listOf("-shell-escape", "-synctex=1", "-interaction=nonstopmode", "-halt-on-error")
        set(value) {
            field = value
            extraArgumentsChanged = true
        }

    private var quietChanged = false
    var quiet: Boolean? = null
        set(value) {
            field = value
            quietChanged = true
        }

    private fun fromName(extension: String) = when {
        name.endsWith(".tex") -> name.substring(0, endIndex = name.length - 4) + ".$extension"
        else -> "$name.$extension"
    }

    fun fileFromName(extension: String) = project.file(fromName(extension))

    /**
     * Apply this builder's changes to an artifact.
     */
    internal fun applyTo(artifact: LatexArtifact) {
        val bib = bib
        if (bibChanged && bib != null) {
            artifact.bib.set(fileFromName(bib))
        }
        val dependsOn = dependsOn
        if (dependsOnChanged) {
            artifact.dependsOn.set(dependsOn)
        }
        val images = images
        if (imagesChanged) {
            artifact.imageFiles.set(images)
        }
        val extraArguments = extraArguments
        if (extraArgumentsChanged) {
            artifact.extraArgs.set(extraArguments)
        }
        val quiet = quiet
        if (quietChanged && quiet != null) {
            artifact.quiet.set(quiet)
        }
    }
}