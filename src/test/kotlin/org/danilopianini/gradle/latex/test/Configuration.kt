package org.danilopianini.gradle.latex.test

import com.uchuhimo.konf.ConfigSpec

data class Configuration(val tasks: List<String>, val options: List<String>) {
    companion object : ConfigSpec() {
        val tasks by required<List<String>>()
        val options by optional<List<String>>(emptyList())
    }
}