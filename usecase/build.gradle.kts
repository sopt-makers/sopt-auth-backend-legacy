plugins {
    java
    id("org.springframework.boot") apply true
    id("io.spring.dependency-management") apply true
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":support"))
    implementation(project(":infra"))

    //spring
    implementation("org.springframework.boot:spring-boot-starter")
    implementation ("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    //test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootJar{
    enabled = false
}