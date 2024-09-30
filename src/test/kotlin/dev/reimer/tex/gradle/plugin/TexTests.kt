package dev.reimer.tex.gradle.plugin

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.file.shouldBeAFile
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TexTests : StringSpec({

    val configs = setOf(
        ElsevierTest,
        LatexGuideTest,
        SimpleLatexTest,
        SimpleLatexIncludeTest,
        SimpleLatexBibtexTest,
        SimpleLatexBibtexIncludeTest,
        SimpleLatexBiberTest,
        SimpleLatexBiberIncludeTest
    )
    configs.forEach { test ->
        log.debug("Test to be executed: $test from ${test.directory}")

        temporaryFolder {
            // Copy sample dir.
            test.directory.copyRecursively(root)

            // Create build file.
            newFile("build.gradle.kts").writeText(test.buildFile)

            log.debug("Test has been copied into $root and is ready to get executed")

            test.description {
                // Delete expected files before build.
                test.expectation.file_exists.forEach {
                    root.resolve(it).delete()
                }

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
                    root.resolve(it).apply {
                        shouldExist()
                        shouldBeAFile()
                    }
                }
            }
        }
    }
}) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(TexTests::class.java)
    }
}
