package org.danilopianini.gradle.latex.configuration

import org.danilopianini.gradle.latex.command.PdfCommand
import org.gradle.api.provider.Property

interface PdfCommandConfiguration : CommandConfiguration {

    val pdfCommand: Property<PdfCommand>
}