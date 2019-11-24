package org.danilopianini.gradle.latex.task

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

interface PdflatexConfiguration {

    val tex: RegularFileProperty

    val pdf: RegularFileProperty

    val quiet: Property<Boolean>

    val extraArgs: ListProperty<String>

    val pdflatexCommand: Property<String>
}