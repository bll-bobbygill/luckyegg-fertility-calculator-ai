#!/bin/bash

# Script to start the Spring Boot application

# Default to "serve" if no command is provided
if [ "$1" == "serve" ]; then
    echo "Starting Spring Boot Application"
    exec java -jar /app/fertility-calculator-mojo-0.0.1-SNAPSHOT.jar
else
    echo "Command not recognized"
    exec "$@"
fi
