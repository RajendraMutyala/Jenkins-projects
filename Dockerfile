# Use Java 17 runtime
FROM eclipse-temurin:17-jre

# Set working directory inside container
WORKDIR /app

# Copy Maven-generated JAR into container
COPY target/jenkins-projects-1.0.0.jar app.jar

# Run the Java application
CMD ["java", "-jar", "app.jar"]
