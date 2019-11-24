package org.danilopianini.gradle.latex.test

import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder

fun folder(closure: TemporaryFolder.() -> Unit) = TemporaryFolder().apply {
    create()
    closure()
}
fun TemporaryFolder.file(name: String, content: () -> String) = newFile(name).writeText(content().trimIndent())






