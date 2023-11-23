/*
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TimeTree Application"
include(":app")
*/


/*
rootProject.buildFileName = "build.gradle.kts"

include(":app")*/


pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        jcenter()
        maven(url = "https://jitpack.io")
        maven (url ="https://maven.google.com")
        google()
        mavenCentral()
    }
}
rootProject.name = "TimeTree Application"
include(":app")