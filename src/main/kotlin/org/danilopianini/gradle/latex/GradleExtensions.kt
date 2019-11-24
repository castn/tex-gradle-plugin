package org.danilopianini.gradle.latex

import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import java.io.File

inline fun <reified T> Project.propertyWithDefault(noinline default: () -> T): Property<T> =
    objects.property(T::class.java).convention(provider(default))

inline fun <reified T> Project.propertyWithDefault(defaultProperty: Property<T>): Property<T> =
    objects.property(T::class.java).convention(defaultProperty)

inline fun <reified T> Project.setPropertyWithDefault(noinline default: () -> Iterable<T>): SetProperty<T> =
    objects.setProperty(T::class.java).convention(provider(default))

inline fun <reified T> Project.listPropertyWithDefault(noinline default: () -> Iterable<T>): ListProperty<T> =
    objects.listProperty(T::class.java).convention(provider(default))

inline fun <reified T> Project.listPropertyWithDefault(default: ListProperty<T>): ListProperty<T> =
    objects.listProperty(T::class.java).convention(default)

fun Project.filePropertyWithDefault(default: () -> File?): RegularFileProperty =
    objects.fileProperty().convention(project.layout.file(provider(default)))
