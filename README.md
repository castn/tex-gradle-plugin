[![](https://jitpack.io/v/dev.reimer/latex-gradle-plugin.svg)](https://jitpack.io/#dev.reimer/latex-gradle-plugin)

# latex-gradle-plugin<sup>[α](#status-α)</sup>

A Gradle plugin for building LaTeX projects.

## Gradle Dependency

This library is available on [**jitpack.io**](https://jitpack.io/#dev.reimer/latex-gradle-plugin).  
Add this in your `build.gradle.kts` or `build.gradle` file:

<details open><summary>Kotlin</summary>

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("dev.reimer:latex-gradle-plugin:<version>")
}
```

</details>

<details><summary>Groovy</summary>

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'dev.reimer:latex-gradle-plugin:<version>'
}
```

</details>

## Usage

### Configuring builds

Build LaTeX files by registering tasks:

<details open><summary>Kotlin</summary>

```kotlin
task.register<LatexTask>("buildLatexFile") {
    tex.set(file("sample.tex"))
}
```

</details>

<details><summary>Groovy</summary>

```groovy
task buildLatexFile(type: LatexTask) {
    tex = file("sample.tex")
}
```

</details>

(You may need to import [`LatexTask`](src/main/kotlin/dev/reimer/latex/gradle/plugin/task/LatexTask.kt))

This configuration autodetects if a `sample.bib` is present, 
and builds the resulting `sample.pdf`.

You can register as many latex build tasks as you like.
In case your files are in a sub-directory, specify the path to the source files.

### Options

Global options can be specified directly in the `latex` block.

<details open><summary>Kotlin</summary>

```kotlin
latex {
    quiet.set(true)
    overwrite.set(true)
    auxDirectory.set(file("auxil"))
    pdfCommand.set(PdflatexCommand)
    bibliographyCommand.set(BibtexCommand)
    convertImagesCommand.set(InkscapeCommand)
}
```

</details>

<details><summary>Groovy</summary>

```groovy
latex {
    quiet = true
    overwrite = true
    auxDirectory = file("auxil")
    pdfCommand = PdflatexCommand.INSTANCE
    bibliographyCommand = BibtexCommand.INSTANCE
    convertImagesCommand = InkscapeCommand.INSTANCE
}
```

</details>

All options can also be applied to each task individually.

### Building multiple projects in order

Task dependencies (`dependsOn`) can be used to specify dependencies among LaTeX tasks.
In the following example, `project1.tex` and `project2.tex` can get built in parallel,
while `project3.tex` build can start only after both the former completed successfully.

<details open><summary>Kotlin</summary>

```kotlin
task.register<LatexTask>("buildLatexProject1") {
    tex.set(file("project1.tex"))
}
task.register<LatexTask>("buildLatexProject2") {
    tex.set(file("project2.tex"))
}
task.register<LatexTask>("buildLatexProject3") {
    tex.set(file("project3.tex"))
    dependsOn("buildLatexProject1", "buildLatexProject2")
}
```

</details>

<details><summary>Groovy</summary>

```groovy
task buildLatexProject1(type: LatexTask) {
    tex = file("project1.tex")
}
task buildLatexProject2(type: LatexTask) {
    tex = file("project2.tex")
}
task buildLatexProject3(type: LatexTask) {
    tex = file("project3.tex")
    dependsOn buildLatexProject1, buildLatexProject2
}
```

</details>

## Development

When building and testing this library, make sure to clone the submodules.
Those are LaTeX projects, used for integration tests.

## Fork

This repo is forked from [@DanySK's gradle-latex](https://github.com/DanySK/gradle-latex) project.
His project's code (until commit `4d0243b`) 
is licensed under the [Apache License 2.0](https://github.com/DanySK/gradle-latex/blob/master/LICENSE).
You may use [this library](.) under the terms of the [MIT License](LICENSE).

## Thanks

I am grateful to @DanySK for his work on the forked library, which was the base for this re-implementation.

## Status α

⚠️ _Warning:_ This project is in an experimental alpha stage:
- The API may be changed at any time without further notice.
- Development still happens on `master`.
- Pull Requests are highly appreciated!
