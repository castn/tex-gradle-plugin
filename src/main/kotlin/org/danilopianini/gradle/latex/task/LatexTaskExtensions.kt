package org.danilopianini.gradle.latex.task

import org.danilopianini.gradle.latex.Latex
import org.danilopianini.gradle.latex.LatexExtension
import org.gradle.api.Task
import org.gradle.kotlin.dsl.get

val Task.latexExtension: LatexExtension
    get() = project.extensions[Latex.EXTENSION_NAME] as LatexExtension