package org.danilopianini.gradle.latex.configuration

import org.gradle.api.provider.Property

interface ConvertImagesCommandConfiguration {

    val inkscapeCommand: Property<String>
}