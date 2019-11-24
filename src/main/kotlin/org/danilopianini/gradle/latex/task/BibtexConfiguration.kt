package org.danilopianini.gradle.latex.task

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property

interface BibtexConfiguration {

    val aux: RegularFileProperty

    val bib: RegularFileProperty

    val bbl: RegularFileProperty

    val bibtexCommand: Property<String>
}