package org.danilopianini.gradle.latex.configuration

import org.danilopianini.gradle.latex.command.ConvertImagesCommand
import org.gradle.api.provider.Property

interface ConvertImagesCommandConfiguration : CommandConfiguration {

    val convertImagesCommand: Property<ConvertImagesCommand>
}