FROM maven:3.9.9-eclipse-temurin-21
WORKDIR /app
COPY pom.xml .
COPY src ./src
CMD ["sh", "-c", "rm -rf /temp/build-output/* && mkdir -p /temp/build-output && mvn clean package -DskipTests && cp target/*.jar /temp/build-output/"]