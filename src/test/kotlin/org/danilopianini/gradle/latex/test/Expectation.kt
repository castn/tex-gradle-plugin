package org.danilopianini.gradle.latex.test

import com.uchuhimo.konf.ConfigSpec

data class Expectation(val file_exists: List<String>, val success: List<String>, val failure: List<String>) {
    companion object : ConfigSpec() {
        val file_exists by optional<List<String>>(emptyList())
        val success by optional<List<String>>(emptyList())
        val failure by optional<List<String>>(emptyList())
    }
}
