package org.danilopianini.gradle.latex.test

import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import java.io.File

fun temporaryFolder(block: TemporaryFolder.() -> Unit) = TemporaryFolder().apply {
    create()
    block()
}

fun ClassLoader.getResourceFile(name: String): File {
    val resource = checkNotNull(getResource(name))
    return File(resource.toURI())
}

inline fun <reified T : Any> T.getResourceFile(name: String) = classLoader.getResourceFile(name)

inline val <reified T : Any> T.classLoader
    get() = checkNotNull(T::class.java.classLoader)
