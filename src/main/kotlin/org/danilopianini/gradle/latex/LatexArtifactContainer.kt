package org.danilopianini.gradle.latex

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

internal class LatexArtifactContainer(
    private val project: Project
) : NamedDomainObjectContainer<LatexArtifact> by project.container(
    LatexArtifact::class.java,
    { name -> LatexArtifact(project, name) }
)