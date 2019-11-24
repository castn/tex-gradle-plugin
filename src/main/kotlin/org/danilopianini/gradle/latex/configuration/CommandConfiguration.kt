package org.danilopianini.gradle.latex.configuration

import org.gradle.api.provider.Property

interface CommandConfiguration {

    val quiet: Property<Boolean>
}