package dev.reimer.tex.gradle.plugin

object SimpleLatexBibtexIncludeTest : Test {

    override val directory = getResourceFile("simple-latex-bibtex-include")
    override val configuration = Configuration(
        tasks = listOf("build"),
        options = listOf("--rerun-tasks")
    )
    override val expectation = Expectation(
        file_exists = listOf("out/main.pdf"),
        success = listOf("compileSimple"),
        failure = emptyList()
    )
    override val description = "Simple LaTeX project with Bibtex bibliography and include should build."
    override val buildFile = """
        import dev.reimer.tex.gradle.plugin.task.TexCompile

        plugins {
            id("dev.reimer.tex")
        }

        tex {
            quiet.set(false)
        }
        
        tasks {
            register<TexCompile>("compileSimple") {
                source(projectDir)
                exclude("part.tex")
            }
        }
    """.trimIndent()
}