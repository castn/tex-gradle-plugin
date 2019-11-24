package org.danilopianini.gradle.latex

import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.get

/**
 * Gradle task to run pdflatex on a TeX file.
 * One such task is created for each Latex artifact.
 * 
 * @author csabasulyok
 */
internal open class PdfLatexTask : LatexTask() {

    private val extension = project.extensions[Latex.EXTENSION_NAME] as LatexExtension

    init {
        description = "Uses pdflatex to compile ${artifact.tex} into ${artifact.pdf.get()}"
    }

  /**
   * Main task action.
   * Empties auxiliary directory.
   */
  @TaskAction
  fun pdfLatex() {
    Latex.LOG.info("Executing ${extension.pdfLatexCommand.get()} for {}", artifact.tex)
    val command = StringBuilder(extension.pdfLatexCommand.get())
        .append(if (artifact.quiet.get()) " -quiet " else " ")
        .append(artifact.extraArgs.get().joinToString(" "))
      .append(' ')
        .append(artifact.tex.get().absolutePath)
      .toString()
    Latex.LOG.debug("Prepared command {}", command)
    command.runScript()
    command.runScript()
  }

}