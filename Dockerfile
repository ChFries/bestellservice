# Start from an official JDK image
FROM amazoncorretto:17

# Set working directory inside the container
WORKDIR /app

# Copy the built jar into the container
COPY target/bestellservice-0.0.1.jar app.jar

# Expose the port your app runs on (Spring Boot default is 8080)
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]