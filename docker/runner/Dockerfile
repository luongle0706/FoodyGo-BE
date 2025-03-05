FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
EXPOSE 8080
CMD ["sh", "-c", "ls /temp/build-output && cp /temp/build-output/*.jar app.jar && java -jar app.jar"]