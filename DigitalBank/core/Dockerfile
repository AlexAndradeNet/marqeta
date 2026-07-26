# Stage 1: Build the application
FROM public.ecr.aws/docker/library/maven:3.8.3-amazoncorretto-17 AS build
WORKDIR /app
# Copy source
COPY src /app/src
# Copy pom
COPY pom.xml /app
# Disable git hooks
RUN sed -i "s/<!-- Git Hooks -->/<!-- Git Hooks --><!--/g" "pom.xml"
RUN sed -i "s/<!-- Sonarqube and Test -->/--><!-- Sonarqube and Test -->/g" "pom.xml"
# Copy environment files
COPY .env /app/.env
RUN sed -i "s/localhost/postgres/g" ".env"
# Build
RUN mvn dependency:go-offline dependency:resolve dependency:resolve-plugins
RUN mvn package spring-boot:repackage

# Stage 2: Create a lightweight runtime image
FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-al2-jdk AS runtime
WORKDIR /app
COPY --from=build ["/app/target/core*.jar", "/app/app.jar"]
COPY --from=build ["/app/.env*", "/app/"]
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
