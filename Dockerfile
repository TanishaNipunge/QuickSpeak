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
# Use OpenJDK 17 as the base image

# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the project files into the container
COPY . .

# Make mvnw executable
RUN chmod +x mvnw

# Clean and build the application without tests
RUN ./mvnw clean package -DskipTests

# Expose the Spring Boot default port
EXPOSE 8080

# Run the JAR (replace with the correct name if different)
ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
