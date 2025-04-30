pluginManagement {
    includeBuild("build-logic")

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
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NapzakMarket"
include(
    ":app",
)
include(
    ":core:common",
    ":core:designsystem",
    ":core:util",
)
include(
    ":data:remote",
    ":data:local",
    ":data:dummy",
    ":data:genre",
)
include(
    ":domain:dummy",
    ":domain:genre",
)
include(
    ":feature:main",
    ":feature:dummy",
    ":feature:home",
    ":feature:mypage",
    ":feature:explore",
    ":feature:onboarding",
    ":feature:search",
    ":feature:report",
    ":feature:registration",
    ":feature:store",
    ":feature:detail",
)
