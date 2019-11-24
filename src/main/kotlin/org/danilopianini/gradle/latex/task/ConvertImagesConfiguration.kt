package org.danilopianini.gradle.latex.task

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property

interface ConvertImagesConfiguration {

    val images: ConfigurableFileCollection

    val inkscapeCommand: Property<String>
}