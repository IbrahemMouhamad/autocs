# Copyright (C) 2022 Ibrahem Mouhamad
#
# SPDX-License-Identifier: MIT

FROM maven:3.8.5-openjdk-17 AS build-image

COPY core /tmp/core/
WORKDIR /tmp/core
RUN mvn clean install -Dmaven.test.skip

COPY backend /tmp/backend/
WORKDIR /tmp/backend
RUN mvn clean install -Pprod

FROM openjdk:17-slim-buster AS autocs_server
COPY --from=build-image /tmp/backend/target/backend-0.0.1.jar app.jar
COPY data /tmp/data/

RUN sh -c 'touch /app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
