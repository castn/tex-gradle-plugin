package org.danilopianini.gradle.latex.configuration

import org.gradle.api.provider.Property

interface BibtexCommandConfiguration {

    val bibtexCommand: Property<String>
}