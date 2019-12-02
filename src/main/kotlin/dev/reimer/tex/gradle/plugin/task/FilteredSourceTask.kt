package dev.reimer.tex.gradle.plugin.task

import dev.reimer.tex.gradle.plugin.filterAsTree
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.tasks.*
import java.io.FileFilter
import java.io.FilenameFilter

abstract class FilteredSourceTask : SourceTask() {

    @get:Optional
    @get:Nested
    open val fileFilter: FileFilter? = null

    @get:Optional
    @get:Nested
    open val filenameFilter: FilenameFilter? = null

    @get:Optional
    @get:Nested
    open val fileTreeElementFilter: ((FileTreeElement) -> Boolean)? = null

    /**
     * Returns the source for this task,
     * after the include and exclude patterns, and the file and filename filters have been applied.
     * Ignores source files which do not exist.
     *
     *
     *
     * The [PathSensitivity] for the sources is configured to be [PathSensitivity.ABSOLUTE].
     * If your sources are less strict, please change it accordingly by overriding this method in your subclass.
     *
     *
     * @return The source.
     */
    @InputFiles
    @SkipWhenEmpty
    @PathSensitive(PathSensitivity.ABSOLUTE)
    override fun getSource(): FileTree {
        return super.getSource()
            .filterAsTree { element ->
                val file = element.file
                val fileFilter = fileFilter
                val filenameFilter = filenameFilter
                val fileTreeElementFilter = fileTreeElementFilter
                (fileFilter == null || fileFilter.accept(file)) &&
                        (filenameFilter == null || filenameFilter.accept(file.parentFile, file.name)) &&
                        (fileTreeElementFilter == null || fileTreeElementFilter(element))
            }
            .asFileTree
    }
}