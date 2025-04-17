# Use a lightweight OpenJDK base image
FROM openjdk:17-jdk-slim

# Install netcat for port checking
RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy JAR and wait script
COPY target/*.jar app.jar
COPY wait-for-mysql.sh .

# Make the script executable
RUN chmod +x wait-for-mysql.sh

# Expose backend port
EXPOSE 8000

# Run the wait script, then the app
ENTRYPOINT ["./wait-for-mysql.sh"]
