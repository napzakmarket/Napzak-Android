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
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
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
    ":core:ui_util",
)

include(
    ":data:remote",
    ":data:local",
    ":data:dummy",
    ":data:store",
    ":data:product",
    ":data:registration",
    ":data:genre",
    ":data:banner",
    ":data:report",
    ":data:interest",
    ":data:presigned-url",
)

include(
    ":domain:dummy",
    ":domain:store",
    ":domain:product",
    ":domain:registration",
    ":domain:genre",
    ":domain:banner",
    ":domain:report",
    ":domain:interest",
    ":domain:presigned-url",
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
    ":feature:splash",
    ":feature:login",
    ":feature:wishlist",
)
