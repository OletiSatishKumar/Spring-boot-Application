# Use a lightweight OpenJDK image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from target/ to the container
COPY target/*.jar app.jar

# Expose the application port (optional but useful)
EXPOSE 8000

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
