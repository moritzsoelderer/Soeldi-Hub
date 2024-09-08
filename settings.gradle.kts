rootProject.name = "SoeldiHub"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("junit-jupiter", "5.10.2")
            version("mysql-connector", "9.0.0")
            version("javafx-media", "24-ea+5")

            version("moduleplugin", "1.8.12")
            version("javafxplugin", "0.0.13")
            version("jlink", "2.25.0")

            library("msyql-connector", "com.mysql", "mysql-connector-j").versionRef("mysql-connector")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit-jupiter")
            library("javafx-media", "org.openjfx", "javafx-media").versionRef("javafx-media")

            plugin("moduleplugin", "org.javamodularity.moduleplugin").versionRef("moduleplugin")
            plugin("javafxplugin", "org.openjfx.javafxplugin").versionRef("javafxplugin")
            plugin("jlink", "org.beryx.jlink").versionRef("jlink")
        }
    }
}