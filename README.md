# latex-gradle-plugin

A Gradle plugin for building LaTeX projects.

## Usage

### Importing the plugin

TODO

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
