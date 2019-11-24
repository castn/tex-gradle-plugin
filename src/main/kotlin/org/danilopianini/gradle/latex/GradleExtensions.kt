package org.danilopianini.gradle.latex

import org.gradle.api.Project
import java.io.File

inline fun <reified T> Project.propertyWithDefault(noinline default: () -> T) =
    objects.property(T::class.java).apply { convention(provider(default)) }

inline fun <reified T> Project.setPropertyWithDefault(noinline default: () -> Iterable<T>) =
    objects.setProperty(T::class.java).apply { convention(provider(default)) }

inline fun <reified T> Project.listPropertyWithDefault(noinline default: () -> Iterable<T>) =
    objects.listProperty(T::class.java).apply { convention(provider(default)) }

fun Project.filePropertyWithDefault(default: () -> File?) =
    objects.fileProperty().apply { convention(project.layout.file(provider(default))) }
