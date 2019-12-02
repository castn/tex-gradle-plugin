package dev.reimer.tex.gradle.plugin

import dev.reimer.tex.gradle.plugin.internal.FilteredFileTree
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.*
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.property
import java.io.File

inline fun <reified T> Project.property(): Property<T> = objects.property()

@Suppress("UnstableApiUsage")
inline fun <reified T> Project.property(default: Provider<T>): Property<T> =
    property<T>().convention(default)

inline fun <reified T> Project.property(noinline default: () -> T): Property<T> =
    property<T>(provider(default))

@Suppress("UnstableApiUsage")
fun Project.fileProperty(): RegularFileProperty = objects.fileProperty()

@Suppress("UnstableApiUsage")
fun Project.directoryProperty(): DirectoryProperty = objects.directoryProperty()

fun Project.directoryProperty(default: Provider<Directory>): DirectoryProperty =
    directoryProperty().convention(default)

fun Project.directoryProperty(default: () -> File?): DirectoryProperty =
    directoryProperty(project.layout.dir(provider(default)))

@Suppress("UnstableApiUsage")
fun Project.fileCollection(): ConfigurableFileCollection = objects.fileCollection()

@Suppress("UnstableApiUsage")
fun Project.sourceDirectorySet(name: String, displayName: String = name): SourceDirectorySet =
    objects.sourceDirectorySet(name, displayName)

fun File.withExtension(extension: String) = resolveSibling("$nameWithoutExtension.$extension")

internal val Task.texExtension: TexExtension
    get() = project.extensions.getByName(TexPlugin.EXTENSION_NAME) as TexExtension

operator fun File.contains(other: File): Boolean {
    val parent = other.parentFile ?: return false
    if (parent == this) return true
    return contains(parent)
}

/**
 * Restricts the contents of this collection to those files which match the given criteria. The filtered
 * collection is live, so that it reflects any changes to this collection.
 *
 * @param filter The criteria to use to select the contents of the filtered collection.
 * @return The filtered collection.
 */
fun FileTree.filterAsTree(filter: (FileTreeElement) -> Boolean): FileTree = FilteredFileTree(this, filter)
