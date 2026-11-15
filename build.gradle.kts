plugins {
    id("java")
    id("io.qameta.allure") version "2.12.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

allure {
    report {
        version.set("2.29.0")
    }
    adapter {
        aspectjWeaver.set(true)
        frameworks {
            junit5 {
                adapterVersion.set("2.29.0")
            }
        }
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("org.slf4j:slf4j-simple:2.0.18")
    testImplementation("io.qameta.allure:allure-junit5:2.29.0")
    implementation("io.qameta.allure:allure-rest-assured:2.29.0")
    implementation("io.qameta.allure:allure-selenide:2.29.0")
    implementation("io.rest-assured:rest-assured:5.5.6")
    implementation(files("libs/oa_at_train_project-1.0-SNAPSHOT.jar"))
    implementation("org.seleniumhq.selenium:selenium-java:4.49.0")
    implementation("com.github.javafaker:javafaker:1.0.2")
    implementation("com.codeborne:selenide:7.19.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.22.3")
    implementation("org.aeonbits.owner:owner:1.0.12")
}


tasks.test {
    useJUnitPlatform()
}