package org.danilopianini.gradle.latex.configuration

import org.gradle.api.file.ConfigurableFileCollection

interface ConvertImagesConfiguration : ConvertImagesCommandConfiguration {

    /**
     * Collection of image files or directories with images
     * which have to be transformed because LaTeX cannot use them directly (e.g. svg, emf).
     * These are transformed to PDFs which then can be included in pdflatex.
     */
    val images: ConfigurableFileCollection
}