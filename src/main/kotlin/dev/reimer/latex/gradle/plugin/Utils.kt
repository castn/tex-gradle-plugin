package dev.reimer.latex.gradle.plugin

import org.gradle.api.Incubating
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty
import java.io.File

@Incubating
inline fun <reified T> Project.propertyWithDefault(noinline default: () -> T): Property<T> =
    objects.property(T::class.java).convention(provider(default))

@Incubating
inline fun <reified T> Project.propertyWithDefault(defaultProperty: Property<T>): Property<T> =
    objects.property(T::class.java).convention(defaultProperty)

inline fun <reified T> Project.setPropertyWithDefault(noinline default: () -> Iterable<T>): SetProperty<T> =
    objects.setProperty(T::class.java).convention(provider(default))

inline fun <reified T> Project.listPropertyWithDefault(noinline default: () -> Iterable<T>): ListProperty<T> =
    objects.listProperty(T::class.java).convention(provider(default))

inline fun <reified T> Project.listPropertyWithDefault(default: ListProperty<T>): ListProperty<T> =
    objects.listProperty(T::class.java).convention(default)

@Incubating
fun Project.filePropertyWithDefault(default: () -> File?): RegularFileProperty =
    objects.fileProperty().convention(project.layout.file(provider(default)))

@Incubating
fun Project.directoryPropertyWithDefault(default: () -> File?): DirectoryProperty =
    objects.directoryProperty().convention(project.layout.dir(provider(default)))

@Incubating
fun Project.directoryPropertyWithDefault(default: Provider<Directory>): DirectoryProperty =
    objects.directoryProperty().convention(default)

fun File.withExtension(extension: String): File {
    val newName = "$nameWithoutExtension.$extension"
    return File(parent, newName)
}

internal val Task.latexExtension: LatexExtension
    get() = project.extensions.getByName(LatexPlugin.EXTENSION_NAME) as LatexExtension

