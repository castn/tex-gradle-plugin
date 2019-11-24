package org.danilopianini.gradle.latex.test

import com.uchuhimo.konf.ConfigSpec

object Root : ConfigSpec("") {
    val tests by required<List<Test>>()
}
