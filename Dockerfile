# Use OpenJDK image
FROM openjdk:17-jdk-slim

# Add the application's JAR file
COPY target/query-executor.jar app.jar

# Expose the port
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]