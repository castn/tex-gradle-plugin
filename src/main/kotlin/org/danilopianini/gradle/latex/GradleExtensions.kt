package org.danilopianini.gradle.latex

import org.gradle.api.Project
import org.gradle.api.provider.HasMultipleValues
import org.gradle.api.provider.Property

inline fun <reified T> Project.propertyWithDefault(noinline default: () -> T) =
    objects.property(T::class.java).apply { convention(project, default) }

inline fun <reified T> Project.setPropertyWithDefault(noinline default: () -> Iterable<T>) =
    objects.setProperty(T::class.java).apply { convention(project, default) }

inline fun <reified T> Project.listPropertyWithDefault(noinline default: () -> Iterable<T>) =
    objects.listProperty(T::class.java).apply { convention(project, default) }

inline fun <reified T> Property<T>.convention(project: Project, noinline provider: () -> T): Property<T> =
    convention(project.provider(provider))

inline fun <reified T> HasMultipleValues<T>.convention(
    project: Project,
    noinline provider: () -> Iterable<T>
): HasMultipleValues<T> =
    convention(project.provider(provider))
