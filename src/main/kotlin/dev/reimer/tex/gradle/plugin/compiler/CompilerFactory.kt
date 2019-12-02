package dev.reimer.tex.gradle.plugin.compiler

import dev.reimer.tex.gradle.plugin.compiler.bibliography.Biber
import dev.reimer.tex.gradle.plugin.compiler.bibliography.BibliographyCompiler
import dev.reimer.tex.gradle.plugin.compiler.bibliography.BibliographyCompilerType
import dev.reimer.tex.gradle.plugin.compiler.bibliography.Bibtex
import dev.reimer.tex.gradle.plugin.compiler.image.ImageConverter
import dev.reimer.tex.gradle.plugin.compiler.image.ImageConverterType
import dev.reimer.tex.gradle.plugin.compiler.image.InkscapePdf
import dev.reimer.tex.gradle.plugin.compiler.tex.Pdflatex
import dev.reimer.tex.gradle.plugin.compiler.tex.TexCompiler
import dev.reimer.tex.gradle.plugin.compiler.tex.TexCompilerType
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.create
import java.util.*
import kotlin.reflect.KClass


internal object CompilerFactory {
    private val random = Random()

    private fun generateTempTaskName(): String {
        return "tempTask${System.nanoTime()}${random.nextLong()}"
    }

    private fun <T : Task> Project.tempTask(taskClass: KClass<out T>): T {
        return tasks.create(generateTempTaskName(), taskClass)
    }

    fun createBibliographyCompiler(project: Project, compiler: BibliographyCompilerType): BibliographyCompiler {
        val taskClass = when (compiler) {
            BibliographyCompilerType.BIBER -> Biber::class
            BibliographyCompilerType.BIBTEX -> Bibtex::class
        }
        return project.tempTask(taskClass)
    }

    fun createTexCompiler(project: Project, compiler: TexCompilerType): TexCompiler {
        val taskClass = when (compiler) {
            TexCompilerType.PDFLATEX -> Pdflatex::class
        }
        return project.tempTask(taskClass)
    }

    fun createImageConverter(project: Project, compiler: ImageConverterType): ImageConverter {
        val taskClass = when (compiler) {
            ImageConverterType.INKSCAPE_PDF -> InkscapePdf::class
        }
        return project.tempTask(taskClass)
    }
}