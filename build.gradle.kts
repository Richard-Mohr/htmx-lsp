plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    id("org.jetbrains.intellij") version "1.17.0"
}

group = "com.lsp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

intellij {
    version.set("2023.3.3")
    type.set("IU")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("241.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

abstract class MyCopyTask : DefaultTask() {

    @get:InputDirectory abstract val source: DirectoryProperty

    @get:OutputDirectory abstract val destination: DirectoryProperty

    @get:Inject abstract val fs: FileSystemOperations

    @TaskAction
    fun action() {
        fs.copy {
            from(source)
            into(destination)
        }
    }
}

tasks.register<MyCopyTask>("copySLP") {
    val projectDir = layout.projectDirectory
    source = projectDir.dir("./language-server")
    destination = projectDir.dir("./build/idea-sandbox/plugins/demo/language-server")
}

tasks.named("prepareSandbox") { finalizedBy("copySLP") }
tasks.named("runIde") { dependsOn("copySLP") }