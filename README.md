# latex-gradle-plugin<sup>[α](#status-α)</sup>

A Gradle plugin for building LaTeX projects.

## Usage

### Importing the plugin

#### Gradle Dependency

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

### Configuring the plugin

#### Minimal configuration

```kotlin
latex {
    register("myMainLatexFile")
}
```

This configuration guesses your main `tex` file to be `myMainLatexFile.tex`, autodetects if a `myMainLatexFile.bib` is
present, and builds the resulting `myMainLatexFile.pdf`.

#### Building multiple main files

```kotlin
latex {
    register("oneFile")
    register("anotherFile")
    register("subDir/anotherFile")
}
```

You can register multiple artifacts from the `latex` extension.
In case your files are in a sub-directory, specify the path to the source files.

#### Configuration options

Global options can be specified directly in the latex block.
Per-project options can be specified in a block after the main file name.
The following examples shows the available options and their default values.

```kotlin
latex {
    // TODO
}
```

#### Building multiple projects in order

The `dependsOn` option can be used to specify dependencies among latex tasks.
In the following example, project1 and project2 can get built in parallel,
while project3 build can start only after both the former completed successfully.

```kotlin
latex {
    val project1 = register("project1")
    val project2 = register("project2")
    register("project3") {
        dependsOn.add(project1)
        dependsOn.add(project2)
    }
}
```

## Fork

This repo is forked from [@DanySK's gradle-latex](https://github.com/DanySK/gradle-latex) project.
His project's code (until commit `4d0243b`) 
is licensed under the [Apache License 2.0](https://github.com/DanySK/gradle-latex/blob/master/LICENSE).
You may use [this library](.) under the terms of the [MIT License](LICENSE).

## Status α

⚠️ _Warning:_ This project is in an experimental alpha stage:
- The API may be changed at any time without further notice.
- Development still happens on `master`.
- Pull Requests are highly appreciated!
