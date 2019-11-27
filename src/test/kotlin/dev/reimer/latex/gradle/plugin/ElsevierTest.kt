package dev.reimer.latex.gradle.plugin

object ElsevierTest : Test {
    override val directory = getResourceFile("latex-els-cas-template")
    override val configuration = Configuration(
        tasks = listOf("latex"),
        options = listOf("--rerun-tasks")
    )
    override val expectation = Expectation(
        file_exists = listOf("out/cas-dc-template.pdf", "out/cas-sc-template.pdf", "out/elsdoc-cas.pdf"),
        success = listOf("latex", "latexCasScTemplate", "latexCasDcTemplate", "latexDocElsdocCas"),
        failure = emptyList()
    )
    override val description = "Elsevier LaTeX template should build."
    override val buildFile = """
        import dev.reimer.latex.gradle.plugin.task.LatexTask

        plugins {
            id("dev.reimer.latex-gradle-plugin")
        }
        
        latex {
            quiet.set(false)
            outputDirectory.set(file("out"))
            auxDirectory.set(file("auxil"))
        }
        
        tasks {
            register<LatexTask>("latexCasScTemplate") {
                tex.set(file("cas-sc-template.tex"))
                bib.set(file("cas-refs.bib"))
            }
            register<LatexTask>("latexCasDcTemplate") {
                tex.set(file("cas-dc-template.tex"))
                bib.set(file("cas-refs.bib"))
            }
            register<LatexTask>("latexDocElsdocCas") {
                tex.set(file("doc/elsdoc-cas.tex"))
            }
        }
    """.trimIndent()
}
