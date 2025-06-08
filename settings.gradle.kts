pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // *** THIS IS THE ONLY CHANGE YOU NEED TO MAKE ***
        // It adds the address for the JitPack repository.
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Zahra Muellim PhD ENG"
include(":app")