package dev.reimer.tex.gradle.plugin

object ElsevierTest : Test {
    override val directory = getResourceFile("latex-els-cas-template")
    override val configuration = Configuration(
        tasks = listOf("build"),
        options = listOf("--rerun-tasks")
    )
    override val expectation = Expectation(
        file_exists = listOf("out/cas-dc-template.pdf", "out/cas-sc-template.pdf", "out/doc/elsdoc-cas.pdf"),
        success = listOf("compileTemplates", "compileDoc"),
        failure = emptyList()
    )
    override val description = "Elsevier LaTeX template should build."
    override val buildFile = """
        import dev.reimer.tex.gradle.plugin.task.TexCompile

        plugins {
            id("dev.reimer.tex")
        }
        
        tex {
            quiet.set(false)
        }
        
        tasks {
            register<TexCompile>("compileTemplates") {
                source(projectDir)
                exclude("doc/")
            }
            register<TexCompile>("compileDoc") {
                source(projectDir)
                include("doc/")
            }
        }
    """.trimIndent()
}
