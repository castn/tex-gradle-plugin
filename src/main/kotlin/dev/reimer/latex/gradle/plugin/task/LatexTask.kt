package dev.reimer.latex.gradle.plugin.task

import dev.reimer.latex.gradle.plugin.*
import dev.reimer.latex.gradle.plugin.configuration.LatexTaskConfiguration
import dev.reimer.latex.gradle.plugin.internal.FileExtensions.BIB
import dev.reimer.latex.gradle.plugin.internal.FileExtensions.PDF
import dev.reimer.latex.gradle.plugin.internal.FileExtensions.TEX
import dev.reimer.latex.gradle.plugin.internal.hasBibFile
import dev.reimer.latex.gradle.plugin.internal.texDir
import dev.reimer.latex.gradle.plugin.internal.texFile
import dev.reimer.latex.gradle.plugin.internal.texNameWithoutExtension
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec
import org.gradle.process.internal.ExecAction

open class LatexTask : Exec(), LatexTaskConfiguration {

    @get:Input
    final override val bibliographyCommand = project.propertyWithDefault(latexExtension.bibliographyCommand)

    @get:Input
    final override val convertImagesCommand = project.propertyWithDefault(latexExtension.convertImagesCommand)

    @get:Input
    final override val pdfCommand = project.propertyWithDefault(latexExtension.pdfCommand)

    @get:Input
    final override val quiet = project.propertyWithDefault(latexExtension.quiet)

    @get:Input
    final override val overwrite = project.propertyWithDefault(latexExtension.overwrite)

    @get:OutputDirectory
    final override val outputDirectory = project.directoryPropertyWithDefault { texDir }

    @get:OutputDirectory
    final override val auxDirectory = project.directoryPropertyWithDefault(outputDirectory)

    @get:InputFile
    final override val tex =
        project.filePropertyWithDefault {
            val fileName: String = when {
                name.endsWith(".$TEX") -> name
                else -> "$name.$TEX"
            }
            project.file(fileName)
        }

    @get:Input
    final override val jobName = project.propertyWithDefault { texNameWithoutExtension }

    @get:Optional
    @get:InputFile
    final override val bib =
        project.filePropertyWithDefault {
            val bibFile = project.file(texFile.withExtension(BIB))
            if (bibFile.exists()) bibFile else null
        }

    @get:InputFiles
    final override val images: ConfigurableFileCollection = project.objects.fileCollection()

    @get:OutputFile
    final override val pdf: RegularFileProperty =
        project.filePropertyWithDefault {
            project.file(texFile.withExtension(PDF))
        }

    override fun getGroup() = LatexPlugin.TASK_GROUP
    override fun getDescription() = "Builds the ${jobName.get()} LaTeX artifact."

    @TaskAction
    override fun exec() {
        makeDirs()
        convertImages()
        if (hasBibFile) {
            pdf()
            bibliography()
        }
        pdf()
        pdf()
    }

    private fun makeDirs() {
        outputDirectory.get().asFile.mkdirs()
        auxDirectory.get().asFile.mkdirs()
    }

    private fun convertImages() {
        with(convertImagesCommand.get()) {
            execute {
                configure(this@LatexTask)
            }
        }
    }

    private fun pdf() {
        with(pdfCommand.get()) {
            execute {
                configure(this@LatexTask)
            }
        }
    }

    private fun bibliography() {
        with(bibliographyCommand.get()) {
            execute {
                configure(this@LatexTask)
            }
        }
    }

    private fun execute(configuration: ExecSpec.() -> Boolean): ExecResult? {
        return newExecAction()
            .run {
                val shouldExecute = configuration()
                if (!shouldExecute) return null
                execute()
            }
    }

    private fun newExecAction(): ExecAction {
        return execActionFactory.newExecAction().apply {
            workingDir = texDir
        }
    }
}