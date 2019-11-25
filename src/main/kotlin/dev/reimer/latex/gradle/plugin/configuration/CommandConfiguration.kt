package dev.reimer.latex.gradle.plugin.configuration

import org.gradle.api.provider.Property

interface CommandConfiguration {

    val quiet: Property<Boolean>
}