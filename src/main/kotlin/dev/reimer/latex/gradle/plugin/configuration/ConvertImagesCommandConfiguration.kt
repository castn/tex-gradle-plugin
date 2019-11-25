package dev.reimer.latex.gradle.plugin.configuration

import dev.reimer.latex.gradle.plugin.command.ConvertImagesCommand
import org.gradle.api.provider.Property

interface ConvertImagesCommandConfiguration : CommandConfiguration {

    val convertImagesCommand: Property<ConvertImagesCommand>
}