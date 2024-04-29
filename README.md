# LuckyEgg - Fertility Calculator AI
## How to Create an Inference Endpoint on AWS SageMaker Using an H2O.ai AutoML Trained Model
This repository contains a JAVA Spring Boot project which illustrates how to take an AI model trained with [h2o.ai's AutoML](https://docs.h2o.ai/h2o/latest-stable/h2o-docs/automl.html), exported as a MOJO file, and host it within a Docker container such that it can be deployed as an inference endpoint on AWS SageMaker.

This code is built to expose a Binary Classification AutoML model that is trained to predict the probability of a female producing a at least 1 genetically normal (Euploid) embryo given the following two inputs:
- AMH (real number)
- Age (integer)

This main logic for this project resides in the <b>FertilityCalculatorMojoApplication.java</b> source file. It exposes an /invocations endpoint that accepts the two aforementioned inputs as a JSON POST body and returns a probability score.

NOTE: The model itself is <b>NOT</b> included in this source repository as that is proprietary data. However, you can use the basic framework of this code sample and adapt it to any h2o.ai AutoML trained-model for hosting in AWS SageMaker. To include your own MOJO model file, please place it into the src/main/java/resources/static file path and update the referenced filename in the FertilityCalculatorMojoApplication.java file.

The project is deployed via Dockerfile setup to initialize and run a default 'serve' command that is expected by AWS SageMaker.

## Compiling the Project
This project uses Gradle for compilation, and you can create a JAR file via the following command:
```java
./gradlew build
```

## Building and Running the Docker Container
You can build and run this Docker container locally by executing:
```shell
docker build -t fertility-calculator-mojo .
docker run -p 8080:8080 fertility-calculator-mojo:latest serve
```

## Invoking the inference endpoint locally
Once running locally, you can use the following cURL command to call the /invocations endpoint:
```shell
curl --location 'http://127.0.0.1:8080/invocations' \
--header 'Content-Type: application/json' \
--data '{"age":29, "amh":3}'
```