package dev.reimer.latex.gradle.plugin

object LatexGuideTest : Test {

    override val directory = getResourceFile("latex-guide")
    override val configuration = Configuration(
        tasks = listOf("latex"),
        options = listOf("--rerun-tasks")
    )
    override val expectation = Expectation(
        file_exists = listOf("out/latex.pdf"),
        success = listOf("latex", "latexGuide"),
        failure = emptyList()
    )
    override val description = "LaTeX guide by Mark Gates should build."
    override val buildFile = """
        import dev.reimer.latex.gradle.plugin.task.LatexTask
        
        plugins {
            id("dev.reimer.latex-gradle-plugin")
        }
        
        latex {
            quiet.set(false)
        }
        
        tasks {
            register<LatexTask>("latexGuide") {
                tex.set(file("latex.tex"))
                outputDirectory.set(file("out"))
                auxDirectory.set(layout.buildDirectory.dir("latex"))
            }
        }
    """.trimIndent()
}