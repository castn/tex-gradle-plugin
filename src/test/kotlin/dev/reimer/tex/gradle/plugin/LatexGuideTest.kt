package dev.reimer.tex.gradle.plugin

object LatexGuideTest : Test {

    override val directory = getResourceFile("latex-quick-reference")
    override val configuration = Configuration(
        tasks = listOf("build"),
        options = listOf("--rerun-tasks")
    )
    override val expectation = Expectation(
        file_exists = listOf("output/latex.pdf"),
        success = listOf("compileGuide"),
        failure = emptyList()
    )
    override val description = "LaTeX quick reference by Mark Gates should build."
    override val buildFile = """
        import dev.reimer.tex.gradle.plugin.task.TexCompile

        plugins {
            id("dev.reimer.tex")
        }

        tex {
            quiet.set(false)
        }
        
        tasks {
            register<TexCompile>("compileGuide") {
                source(projectDir)
                destinationDir.set(file("output"))
            }
        }
    """.trimIndent()
}