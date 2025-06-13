# # Dockerfile

# # Use OpenJDK 17 as the base image
# FROM openjdk:17-jdk-slim

# # Set the working directory
# WORKDIR /app

# # Copy the jar file
# #COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
# COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# # Expose port (default Spring root port)
# EXPOSE 8080

# # Command to run the app
# ENTRYPOINT ["java", "-jar", "app.jar"]
# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the pom and source files
COPY . .

# Build the application without running tests
RUN ./mvnw clean package -DskipTests

# Expose port (default Spring Boot port)
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
