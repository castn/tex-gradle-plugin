package dev.reimer.latex.gradle.plugin.configuration

import dev.reimer.latex.gradle.plugin.command.PdfCommand
import org.gradle.api.provider.Property

interface PdfCommandConfiguration : CommandConfiguration {

    val pdfCommand: Property<PdfCommand>
}