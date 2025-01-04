# FROM gradle:latest AS build
# WORKDIR /app
# COPY . .
# RUN ["gradle", "buildFatJar"]

FROM amazoncorretto:21 AS runtime
WORKDIR /app
COPY /build/libs/enshittify-all.jar .
COPY /src/main/resources/ src/main/resources/

ENTRYPOINT ["java", "-jar", "enshittify-all.jar"]