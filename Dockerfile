FROM maven:3-jdk-11 as build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /build/src/
RUN mvn install -Dmaven.test.skip=true

FROM openjdk:11
EXPOSE 8080
COPY --from=build /build/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]