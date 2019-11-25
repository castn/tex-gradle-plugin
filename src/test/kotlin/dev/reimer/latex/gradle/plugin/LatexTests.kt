package dev.reimer.latex.gradle.plugin

import io.kotlintest.matchers.file.shouldBeAFile
import io.kotlintest.matchers.file.shouldExist
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.slf4j.LoggerFactory

class LatexTests : StringSpec({

    val elsevierCasConfig = Test(
        directory = getResourceFile("elsevier-cas"),
        configuration = Configuration(
            tasks = listOf("latex"),
            options = listOf("--rerun-tasks")
        ),
        expectation = Expectation(
            file_exists = listOf("cas-dc-template.pdf", "cas-sc-template.pdf", "doc/elsdoc-cas.pdf"),
            success = listOf("latex", "bibliographyCasDcTemplate", "bibliographyCasScTemplate"),
            failure = emptyList()
        ),
        description = "Elsevier Latex Template should build"
    )
    val latexGuideConfig = Test(
        directory = getResourceFile("latex-guide"),
        configuration = Configuration(
            tasks = listOf("latex"),
            options = listOf("--rerun-tasks")
        ),
        expectation = Expectation(
            file_exists = listOf("latex.pdf"),
            success = listOf("latex", "pdfLatex"),
            failure = emptyList()
        ),
        description = "latex guide by M. Gates should build"
    )
    val configs = setOf(elsevierCasConfig, latexGuideConfig)
    configs.forEach { test ->
        log.debug("Test to be executed: $test from ${test.directory}")
        val testFolder = temporaryFolder {
            test.directory.copyRecursively(root)
            log.debug("Test has been copied into $root and is ready to get executed")

            test.description {
                val result = GradleRunner.create()
                    .withProjectDir(root)
                    .withPluginClasspath()
                    .withArguments(test.configuration.tasks + test.configuration.options + "--rerun-tasks" + "--stacktrace")
                    .build()
                println(result.tasks)
                println(result.output)
                test.expectation.success.forEach {
                    val task = result.task(":$it")
                    require(task != null) {
                        "Task $it was not present among the executed tasks"
                    }
                    task.outcome shouldBe TaskOutcome.SUCCESS
                }
                test.expectation.file_exists.forEach {
                    with(root.resolve(it)) {
                        shouldExist()
                        shouldBeAFile()
                    }
                }
            }
        }
        }
}) {
    companion object {
        val log = LoggerFactory.getLogger(LatexTests::class.java)
    }
}