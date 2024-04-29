# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY ./build/libs/fertility-calculator-mojo-0.0.1-SNAPSHOT.jar /app/fertility-calculator-mojo-0.0.1-SNAPSHOT.jar

# Copy the serve script into the container
COPY serve.sh /app/serve.sh

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Make the script executable and set it as the entry point
RUN chmod +x /app/serve.sh
ENTRYPOINT ["/app/serve.sh"]

# Default command
CMD ["serve"]