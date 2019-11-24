package org.danilopianini.gradle.latex.test

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import io.github.classgraph.ClassGraph
import io.kotlintest.matchers.file.shouldBeAFile
import io.kotlintest.matchers.file.shouldExist
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.slf4j.LoggerFactory
import java.io.File

class LatexTests : StringSpec({
    val pluginClasspathResource = ClassLoader.getSystemClassLoader()
        .getResource("plugin-classpath.txt")
        ?: throw IllegalStateException("Did not find plugin classpath resource, run \"testClasses\" build task.")
    val classpath = pluginClasspathResource.openStream().bufferedReader().use { reader ->
        reader.readLines().map { File(it) }
    }
    val scan = ClassGraph()
        .enableAllInfo()
        .whitelistPackages("org.danilopianini.gradle.latex.test")
        .scan()
    scan.getResourcesWithLeafName("test.yaml")
        .flatMap {
            log.debug("Found test list in $it")
            val yamlFile = File(it.classpathElementFile.absolutePath + "/" + it.path)
            val testConfiguration = Config {
                addSpec(Root)
                addSpec(Test)
                addSpec(Expectation)
                addSpec(Configuration)
            }.from.yaml.inputStream(it.open())
            testConfiguration[Root.tests].map { it to yamlFile.parentFile }
        }.forEach { (test, location) ->
            log.debug("Test to be executed: $test from $location")
            val testFolder = folder {
                location.copyRecursively(this.root)
            }
            log.debug("Test has been copied into $testFolder and is ready to get executed")
            test.description {
                val result = GradleRunner.create()
                    .withProjectDir(testFolder.root)
                    .withPluginClasspath()
                    .withArguments(test.configuration.tasks + test.configuration.options)
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
                    with(File("${testFolder.root.absolutePath}/$it")) {
                        shouldExist()
                        shouldBeAFile()
                    }
                }
            }
        }
}) {
    companion object {
        val log = LoggerFactory.getLogger(LatexTests::class.java)
    }
}