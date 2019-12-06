package dev.reimer.tex.gradle.plugin

object SimpleLatexTest : Test {

    override val directory = getResourceFile("simple-latex")
    override val configuration = Configuration(
        tasks = listOf("build"),
        options = listOf("--rerun-tasks")
    )
    override val expectation = Expectation(
        file_exists = listOf("out/main.pdf"),
        success = listOf("compileSimple"),
        failure = emptyList()
    )
    override val description = "Simple LaTeX project should build."
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
            }
        }
    """.trimIndent()
}