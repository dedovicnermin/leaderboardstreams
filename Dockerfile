FROM maven:3.9.3-amazoncorretto-11 AS MAVEN_BUILD
ADD pom.xml pom.xml
RUN mvn clean dependency:go-offline
ADD ./src src
RUN mvn package

FROM openjdk:11.0.16-jre-buster

# add user
ARG APPLICATION_USER=nermdev
RUN useradd --no-create-home -u 1000 $APPLICATION_USER

# config working directory
RUN mkdir /app && \
    chown -R $APPLICATION_USER /app

USER 1000
COPY --from=MAVEN_BUILD --chown=1000:1000 target/leaderboardstreams-jar-with-dependencies.jar /app/app.jar
COPY --from=MAVEN_BUILD --chown=1000:1000 target/classes/log4j.properties /app/log4j.properties
COPY --chown=1000:1000 ./exporter/jmx_prometheus_javaagent-0.19.0.jar /app/jmx_prometheus_javaagent.jar
COPY --chown=1000:1000 ./exporter/kafka_streams.yml /app/kafka_streams.yml

ENV JAVA_TOOL_OPTIONS "-Dcom.sun.management.jmxremote.ssl=false \
 -Dcom.sun.management.jmxremote.authenticate=false \
 -Dcom.sun.management.jmxremote.port=7203 \
 -Dcom.sun.management.jmxremote.rmi.port=7203 \
 -Dcom.sun.management.jmxremote.host=0.0.0.0 \
 -Djava.rmi.server.hostname=${NODE_NAME} \
 -javaagent:/app/jmx_prometheus_javaagent.jar=7778:/app/kafka_streams.yml"

WORKDIR /app

ENV JMX_PORT=7203
ENV EXPORTER_PORT=7778
ENV REST_PORT=7000
EXPOSE 7000
EXPOSE 7203
EXPOSE 7778


ARG CONFIG=/mnt/application/application.properties
ENTRYPOINT ["java", "-jar", "/app/app.jar"]