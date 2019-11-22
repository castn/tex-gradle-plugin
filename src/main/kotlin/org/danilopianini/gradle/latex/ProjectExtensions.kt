package org.danilopianini.gradle.latex

import org.gradle.api.Project
import org.gradle.kotlin.dsl.property

inline fun <reified T> Project.propertyWithDefault(default: T) =
    objects.property<T>().apply { convention(default) }

inline fun <reified T> Project.propertyWithDefault(noinline default: () -> T) =
    objects.property<T>().apply { convention(default()) }
