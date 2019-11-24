package org.danilopianini.gradle.latex

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.kotlin.dsl.get
import java.io.File
import java.io.PrintWriter
import java.util.concurrent.TimeUnit

internal abstract class LatexTask : DefaultTask() {
    /**
     * Latex artifact used to run current task.
     */
    @get:Input
    lateinit var artifact: LatexArtifact
    private val extension = project.extensions[Latex.EXTENSION_NAME] as LatexExtension

    init {
        group = Latex.TASK_GROUP
        description = "Builds the ${artifact.name} LaTeX artifact."
        logging.captureStandardError(LogLevel.ERROR)
        logging.captureStandardOutput(LogLevel.ERROR)
    }

    /**
     * Collection of all files whose change should trigger this task.
     * Collected for Gradle's continuous build feature.
     * Contains the following (based on the Latex artifact):
     * - main TeX file
     * - bib file, if there is one
     * - outputs (PDF) of dependent TeX files
     * - auxiliary files/folders
     */
    @get:InputFiles
    open val inputFiles: FileCollection
        get() = project.files(*artifact.flattenDependencyFiles().toTypedArray())

    /**
     * Output of current task. Not used by task itself.
     * Set for Gradle's continuous build feature.
     */
    @get:OutputFile
    open val pdf
        get() = artifact.pdf

    fun String.runScript(
        terminalEmulator: String = extension.terminalEmulator.get(),
        from: File = project.projectDir,
        waitTime: Long = extension.waitTime.get(),
        waitUnit: TimeUnit = extension.waitUnit.get(),
        whenFails: (Int, String, String) -> Unit = { exit, stdout, stderr ->
            throw GradleException("Process $this terminated with non-zero value $exit.\nStandard error: \n$stderr\n\nStandard output:\n$stdout")
        }
    ) {
        val stderr = createTempFile()
        val stdout = createTempFile()
        val shell = ProcessBuilder(terminalEmulator)
            .directory(from)
            .redirectError(stderr)
            .redirectOutput(stdout)
            .start()
        shell.inputStream.copyTo(System.out)
        PrintWriter(shell.outputStream).use {
            Latex.LOG.debug(
                "Launching {} in {} from directory {}, waiting up to {} {} for termination.",
                this,
                terminalEmulator,
                from,
                waitTime,
                waitUnit
            )
            it.println("$this \n exit")
        }
        val terminatedNaturally = shell.waitFor(waitTime, waitUnit)
        if (!terminatedNaturally) {
            throw GradleException("Process $this stalled and was forcibly terminated due to timeout. If the process was not interactive, consider using longer timeouts.")
        }
        shell.exitValue().takeIf { it != 0 }?.let { whenFails (it, stderr.readText(), stdout.readText()) }
        Latex.LOG.debug("{} completed successfully", this)
    }

}