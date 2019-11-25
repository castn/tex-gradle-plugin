plugins {
  id("org.danilopianini.gradle-latex")
}
latex {
    register("cas-sc-template") {
        bib.set(file("cas-refs.bib"))
    }
    register("cas-dc-template") {
        bib.set(file("cas-refs.bib"))
    }
    register("doc/elsdoc-cas")
}
