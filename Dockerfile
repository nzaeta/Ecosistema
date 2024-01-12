# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the entire resources directory into the container
COPY src/main/resources /app/src/main/resources

# Copy the JAR file and any other necessary files into the container
COPY target/*.jar ./app.jar

# Specify the command to run your Spring Boot application
CMD ["java", "-jar", "app.jar"]
