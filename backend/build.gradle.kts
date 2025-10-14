plugins {
  id("java")
  id("org.springframework.boot") version "3.3.3"
  id("io.spring.dependency-management") version "1.1.6"
  id("org.flywaydb.flyway") version "9.22.3"
}

group = "com.nextra"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

repositories {
  mavenCentral()
}

dependencies {
  /*Standard Dependencies*/
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
  /*Dependencies for DB and migration*/
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.postgresql:postgresql:42.7.3")
  implementation("org.flywaydb:flyway-core:10.20.0")
  implementation("org.hibernate.orm:hibernate-community-dialects:6.4.0.Final")

  // Lombok (code generation)
  compileOnly("org.projectlombok:lombok:1.18.34")
  annotationProcessor("org.projectlombok:lombok:1.18.34")

  // Per i test (opzionale ma consigliato)
  testCompileOnly("org.projectlombok:lombok:1.18.34")
  testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}



tasks.test {
  useJUnitPlatform()
}

flyway {
  url = "jdbc:postgresql://localhost:5432/nextra"
  user = "nextra"
  password = "nextra"
  schemas = arrayOf("public")
  locations = arrayOf("classpath:db/migration")
  cleanDisabled = false /*Solo per locale*/
}
