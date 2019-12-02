[![](https://jitpack.io/v/dev.reimer/tex-gradle-plugin.svg)](https://jitpack.io/#dev.reimer/tex-gradle-plugin)

# tex-gradle-plugin<sup>[α](#status-α)</sup>

A Gradle plugin for building TeX/LaTeX projects.

## Gradle Dependency

This library is available on [**jitpack.io**](https://jitpack.io/#dev.reimer/tex-gradle-plugin).  
Add this in your `build.gradle.kts` or `build.gradle` file:

<details open><summary>Kotlin</summary>

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("dev.reimer:tex-gradle-plugin:<version>")
}
```

</details>

<details><summary>Groovy</summary>

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'dev.reimer:tex-gradle-plugin:<version>'
}
```

</details>

## Usage

### Configuring builds

Build TeX files by registering tasks:

<details open><summary>Kotlin</summary>

```kotlin
tasks.register<TexCompile>("buildTexFile") {
    source("sample.tex")
}
```

</details>

<details><summary>Groovy</summary>

```groovy
task buildTexFile(type: TexCompile) {
    source("sample.tex")
}
```

</details>

(You may need to import [`TexCompile`](src/main/kotlin/dev/reimer/tex/gradle/plugin/task/TexCompile.kt))

This configuration autodetects if a `sample.bib` is present, 
and builds the resulting `sample.pdf`.
Compiled PDFs are stored in the `out/` directory under the project root.
Setting the `destinationDir` property overwrites 
the default output directory.

You can register as many tex compile tasks as you like.
Alternatively, reference a directory, file tree 
or [more](https://docs.gradle.org/current/javadoc/org/gradle/api/Project.html#files-java.lang.Object...-), 
using the `source()` task configuration.

And don't worry about a messy `out/` directory, 
as the folder structure will be preserved.

### Options

Global options can be specified directly in the `tex` block.

<details open><summary>Kotlin</summary>

```kotlin
tex {
    quiet.set(true)
    overwrite.set(true)
    texCompiler.set(TexCompilerType.PDFLATEX)
    bibliographyCompiler.set(BibliographyCompilerType.BIBTEX)
    imageConverter.set(ImageConverterType.INKSCAPE_PDF)
}
```

</details>

<details><summary>Groovy</summary>

```groovy
tex {
    quiet = true
    overwrite = true
    texCompiler = TexCompilerType.PDFLATEX
    bibliographyCompiler = BibliographyCompilerType.BIBTEX
    imageConverter = ImageConverterType.INKSCAPE_PDF
}
```

</details>

All options can also be applied to each task individually.

### Building multiple projects in order

Task dependencies (`dependsOn`) can be used to specify dependencies among TeX tasks.
In the following example, `project1.tex` and `project2.tex` can get built in parallel,
while `project3.tex` build can start only after both the former completed successfully.

<details open><summary>Kotlin</summary>

```kotlin
tasks.register<TexCompile>("buildTexProject1") {
    source("project1/")
}
tasks.register<TexCompile>("buildTexProject2") {
    source("project2/")
}
tasks.register<TexCompile>("buildTexProject3") {
    source("project3/")
    dependsOn("buildTexProject1", "buildTexProject2")
}
```

</details>

<details><summary>Groovy</summary>

```groovy
task buildTexProject1(type: TexCompile) {
    source("project1/")
}
task buildTexProject2(type: TexCompile) {
    source("project2/")
}
task buildTexProject3(type: TexCompile) {
    source("project3/")
    dependsOn buildTexProject1, buildTexProject2
}
```

</details>

## Development

When building and testing this library, make sure to clone the submodules.
Those are TeX projects, used for integration tests.

## Thanks

I am grateful to @DanySK for his work on the forked library, 
which was licensed under the [Apache License 2.0](https://github.com/DanySK/gradle-latex/blob/master/LICENSE).

## Status α

⚠️ _Warning:_ This project is in an experimental alpha stage:
- The API may be changed at any time without further notice.
- Development still happens on `master`.
- Pull Requests are highly appreciated!
