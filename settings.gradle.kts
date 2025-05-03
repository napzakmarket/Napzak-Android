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
    ":data:dummy",
    ":data:remote",
    ":data:local",
    ":data:product",
    ":data:store",
    ":data:registration",
    ":data:genre",
    ":data:banner",
    ":data:report",
    ":data:interest",
    ":data:presigned-url",
)

include(
    ":domain:dummy",
    ":domain:product",
    ":domain:store",
    ":domain:registration",
    ":domain:genre",
    ":domain:banner",
    ":domain:report",
    ":domain:interest",
    ":domain:presigned-url",
)

include(
    ":feature:dummy",
    ":feature:main",
    ":feature:onboarding",
    ":feature:home",
    ":feature:explore",
    ":feature:search",
    ":feature:registration",
    ":feature:mypage",
    ":feature:store",
    ":feature:detail",
    ":feature:report",
)
