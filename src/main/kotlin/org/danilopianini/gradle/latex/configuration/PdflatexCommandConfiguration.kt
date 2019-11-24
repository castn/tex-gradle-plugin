package org.danilopianini.gradle.latex.configuration

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

interface PdflatexCommandConfiguration {

    val pdflatexCommand: Property<String>

    val pdflatexQuiet: Property<Boolean>

    /**
     * Extra arguments to be passed to pdflatex when building this artifact.
     */
    val pdflatexArguments: ListProperty<String>
}