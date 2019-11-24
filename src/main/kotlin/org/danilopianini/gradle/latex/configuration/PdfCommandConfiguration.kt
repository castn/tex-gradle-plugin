package org.danilopianini.gradle.latex.configuration

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

interface PdfCommandConfiguration {

    val pdfCommand: Property<String>

    val pdfQuiet: Property<Boolean>

    /**
     * Extra arguments to be passed to pdflatex when building this artifact.
     */
    val pdfArguments: ListProperty<String>
}