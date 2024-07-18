val libs = libraries
val versionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("8.5.1").apply(false)
    id("com.android.library").version("8.5.1").apply(false)
    kotlin("android").version("1.8.10").apply(false) // Update Kotlin version if necessary
    id("com.android.test").version("8.5.1").apply(false)
    id("de.undercouch.download").version("4.1.2").apply(false) // Update this plugin if necessary
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
